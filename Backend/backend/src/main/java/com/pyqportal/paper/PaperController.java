package com.pyqportal.paper;

import com.pyqportal.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/papers")
@Tag(name = "Papers", description = "Paper upload, search, and management")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a PYQ paper — UPLOADER or ADMIN only")
    public ResponseEntity<PaperResponse> upload(
            @RequestPart("file") MultipartFile file,
            @Valid @ModelAttribute PaperUploadRequest request,
            Authentication authentication
    ) throws IOException {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paperService.uploadPaper(file, request, email));
    }

    @GetMapping
    @Operation(summary = "List / search papers (public). Use ?q= for full-text search via Elasticsearch.")
    public ResponseEntity<PageResponse<PaperResponse>> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Branch branch,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) ExamType examType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(paperService.findPapers(q, branch, subject, year, examType, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single paper by ID (public)")
    public ResponseEntity<PaperResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(paperService.findById(id));
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Get download URL and increment download counter (authenticated)")
    public ResponseEntity<Map<String, String>> download(@PathVariable UUID id) {
        return ResponseEntity.ok(Map.of("url", paperService.getDownloadUrl(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a paper — ADMIN only")
    public ResponseEntity<Void> delete(@PathVariable UUID id) throws IOException {
        paperService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
