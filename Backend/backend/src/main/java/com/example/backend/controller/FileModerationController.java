package com.example.backend.controller;

import com.example.backend.entity.UploadStatus;
import com.example.backend.entity.UploadedFile;
import com.example.backend.repository.UploadedFileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/files")
public class FileModerationController {

    private final UploadedFileRepository repository;

    public FileModerationController(UploadedFileRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/pending")
    public List<UploadedFile> listPending() {
        return repository.findByStatusOrderByUploadedAtDesc(UploadStatus.PENDING);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        return repository.findById(id)
                .map(file -> {
                    file.setStatus(UploadStatus.APPROVED);
                    repository.save(file);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        return repository.findById(id)
                .map(file -> {
                    file.setStatus(UploadStatus.REJECTED);
                    repository.save(file);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}