package com.pyqportal.storage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    @Override
    @SuppressWarnings("unchecked")
    public StorageResult upload(MultipartFile file) throws IOException {
        Map<String, Object> result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "raw",
                        "folder",        "pyqportal/papers"
                )
        );
        String publicId  = (String) result.get("public_id");
        String secureUrl = (String) result.get("secure_url");
        log.info("Uploaded to Cloudinary: publicId={}", publicId);
        return new StorageResult(publicId, secureUrl);
    }

    @Override
    public void delete(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
        log.info("Deleted from Cloudinary: publicId={}", publicId);
    }
}
