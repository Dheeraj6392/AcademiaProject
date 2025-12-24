package com.example.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.backend.entity.UploadedFile;
import com.example.backend.entity.UploadStatus;
import com.example.backend.repository.UploadedFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@Service
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final UploadedFileRepository fileRepository;

    public FileUploadService(Cloudinary cloudinary, UploadedFileRepository fileRepository) {
        this.cloudinary = cloudinary;
        this.fileRepository = fileRepository;
    }

    public Map<String, Object> upload(
            MultipartFile multipartFile,
            String semester,
            String year,
            String courseName
    ) throws Exception {

        // 1️⃣ Convert MultipartFile → File
        File tempFile = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        multipartFile.transferTo(tempFile);

        // 2️⃣ Upload to Cloudinary as RAW
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                tempFile,
                Map.of(
                        "resource_type", "raw",
                        "folder", "academia/papers"
                )
        );

        // 3️⃣ Delete temp file
        tempFile.delete();

        String publicId = (String) uploadResult.get("public_id");
        String secureUrl = (String) uploadResult.get("secure_url");

        // 4️⃣ Save metadata
        UploadedFile record = new UploadedFile();
        record.setPublicId(publicId);
        record.setFileUrl(secureUrl);
        record.setSemester(semester);
        record.setYear(year);
        record.setCourseName(courseName);
        record.setStatus(UploadStatus.PENDING);

        fileRepository.save(record);

        // 5️⃣ Response
        return Map.of(
                "status", "SUCCESS",
                "fileUrl", secureUrl,
                "publicId", publicId,
                "semester", semester,
                "year", year,
                "courseName", courseName
        );
    }
}
