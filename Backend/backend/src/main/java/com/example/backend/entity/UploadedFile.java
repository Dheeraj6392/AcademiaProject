package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "uploaded_files")
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId;

    @Column(length = 1000)
    private String fileUrl;

    // new metadata fields matching your frontend
    private String semester;
    private String year;

    @Column(name = "course_name")
    private String courseName;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private UploadStatus status = UploadStatus.PENDING;
}
