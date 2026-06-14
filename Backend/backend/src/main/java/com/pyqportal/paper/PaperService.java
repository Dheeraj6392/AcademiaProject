package com.pyqportal.paper;

import com.pyqportal.common.PageResponse;
import com.pyqportal.exception.DuplicatePaperException;
import com.pyqportal.exception.ResourceNotFoundException;
import com.pyqportal.indexworker.PaperUploadedEvent;
import com.pyqportal.indexworker.PaperUploadedProducer;
import com.pyqportal.search.SearchService;
import com.pyqportal.storage.StorageResult;
import com.pyqportal.storage.StorageService;
import com.pyqportal.user.User;
import com.pyqportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaperService {

    private final PaperRepository paperRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final PaperUploadedProducer producer;
    private final SearchService searchService;

    /**
     * Upload flow:
     * 1. Compute MD5, check for duplicate
     * 2. Upload to Cloudinary (non-transactional — cannot be rolled back)
     * 3. Save to DB + publish Kafka event
     * 4. On any failure after Cloudinary: compensate by deleting the uploaded file
     *
     * TODO (production): Replace step 3 with an outbox pattern to guarantee
     *   exactly-once event delivery and avoid dual-write inconsistency.
     */
    @CacheEvict(value = "papers", allEntries = true)
    public PaperResponse uploadPaper(MultipartFile file, PaperUploadRequest request,
                                     String uploaderEmail) throws IOException {
        byte[] bytes = file.getBytes();
        String md5 = computeMd5(bytes);

        if (paperRepository.existsByMd5Hash(md5)) {
            throw new DuplicatePaperException("This paper already exists");
        }

        User uploader = userRepository.findByEmail(uploaderEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + uploaderEmail));

        // Step 2 — Cloudinary upload (no rollback possible from here)
        StorageResult cloudinary = storageService.upload(file);

        try {
            // Step 3 — DB save
            Paper paper = Paper.builder()
                    .title(request.getTitle())
                    .subject(request.getSubject())
                    .branch(request.getBranch())
                    .year(request.getYear())
                    .examType(request.getExamType())
                    .fileUrl(cloudinary.secureUrl())
                    .cloudinaryPublicId(cloudinary.publicId())
                    .md5Hash(md5)
                    .uploadedBy(uploader)
                    .build();

            paper = paperRepository.save(paper);

            // Step 3 — Kafka event
            producer.publish(new PaperUploadedEvent(
                    paper.getId().toString(),
                    cloudinary.publicId(),
                    cloudinary.secureUrl(),
                    paper.getTitle(),
                    paper.getSubject(),
                    paper.getBranch().name(),
                    paper.getYear(),
                    paper.getExamType().name()
            ));

            return PaperResponse.from(paper);

        } catch (Exception e) {
            // Compensate: clean up the Cloudinary file and the DB record (if saved)
            log.error("Paper upload failed after Cloudinary upload — compensating", e);
            try {
                storageService.delete(cloudinary.publicId());
            } catch (IOException cleanupEx) {
                log.error("Cloudinary cleanup failed for publicId={}", cloudinary.publicId(), cleanupEx);
            }
            throw new RuntimeException("Paper upload failed, changes reverted: " + e.getMessage(), e);
        }
    }

    @Cacheable(value = "papers",
            key = "T(String).valueOf(#q) + ':' + T(String).valueOf(#branch) + ':' + " +
                  "T(String).valueOf(#subject) + ':' + T(String).valueOf(#year) + ':' + " +
                  "T(String).valueOf(#examType) + ':' + #page + ':' + #size")
    @Transactional(readOnly = true)
    public PageResponse<PaperResponse> findPapers(String q, Branch branch, String subject,
                                                   Integer year, ExamType examType,
                                                   int page, int size) {
        if (q != null && !q.isBlank()) {
            // Full-text path: ask Elasticsearch for IDs, fetch details from PostgreSQL
            List<UUID> ids = searchService.searchPapers(q, branch, subject, year, examType);
            List<PaperResponse> results = paperRepository.findAllByIdsNotDeleted(ids)
                    .stream().map(PaperResponse::from).toList();
            Page<PaperResponse> p = new PageImpl<>(results, PageRequest.of(page, size), results.size());
            return PageResponse.from(p);
        }

        // Filter-only path: query PostgreSQL via JPA Specifications
        Specification<Paper> spec = Specification.where(PaperSpecification.notDeleted())
                .and(PaperSpecification.hasBranch(branch))
                .and(PaperSpecification.hasSubject(subject))
                .and(PaperSpecification.hasYear(year))
                .and(PaperSpecification.hasExamType(examType));

        return PageResponse.from(
                paperRepository.findAll(spec, PageRequest.of(page, size)).map(PaperResponse::from)
        );
    }

    @Transactional(readOnly = true)
    public PaperResponse findById(UUID id) {
        return PaperResponse.from(
                paperRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Paper not found: " + id))
        );
    }

    @Transactional
    public String getDownloadUrl(UUID id) {
        Paper paper = paperRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paper not found: " + id));
        paperRepository.incrementDownloadCount(id);
        return paper.getFileUrl();
    }

    @Transactional
    @CacheEvict(value = "papers", allEntries = true)
    public void softDelete(UUID id) throws IOException {
        Paper paper = paperRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paper not found: " + id));
        paper.setDeletedAt(LocalDateTime.now());
        paperRepository.save(paper);
        searchService.deleteFromIndex(id.toString());
        log.info("Soft-deleted paper {}", id);
    }

    private String computeMd5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return HexFormat.of().formatHex(md.digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 not available", e);
        }
    }
}
