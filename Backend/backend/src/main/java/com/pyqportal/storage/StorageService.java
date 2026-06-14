package com.pyqportal.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    StorageResult upload(MultipartFile file) throws IOException;
    void delete(String publicId) throws IOException;
}
