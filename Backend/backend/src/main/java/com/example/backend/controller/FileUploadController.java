package com.example.backend.controller;

import com.example.backend.service.FileUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "semester", required = false) String semester,
            @RequestParam(value = "year", required = false) String year,
            @RequestParam(value = "courseName", required = false) String courseName
    ) {
        try {
            System.out.println("[UPLOAD] Received file: " + file.getOriginalFilename() +
                               " | size: " + file.getSize() + " bytes");
            Map<String, Object> resp = fileUploadService.upload(file, semester, year, courseName);
            System.out.println("[UPLOAD] Saved with id: " + resp.get("id"));
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            System.err.println("[UPLOAD] Failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // optional management endpoints (list / get / delete) left as-is if you already added them
}
