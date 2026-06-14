package com.pyqportal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "SUPABASE_DB_URL=jdbc:postgresql://localhost:5432/test",
        "SUPABASE_DB_USER=test",
        "SUPABASE_DB_PASSWORD=test",
        "CLOUDINARY_CLOUD_NAME=test",
        "CLOUDINARY_API_KEY=test",
        "CLOUDINARY_API_SECRET=test",
        "JWT_SECRET=test-secret-key-at-least-256-bits-long-for-hmac-sha256"
})
class PYQPortalApplicationTests {

    @Test
    void contextLoads() {
    }
}
