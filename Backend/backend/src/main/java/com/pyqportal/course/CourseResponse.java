package com.pyqportal.course;

import java.time.LocalDateTime;
import java.util.UUID;

public record CourseResponse(
        UUID id,
        String title,
        String description,
        int weeks,
        int lessons,
        double price,
        String imageUrl,
        long enrolledCount,
        boolean enrolled,
        LocalDateTime createdAt
) {
    static CourseResponse from(Course c, long enrolledCount, boolean enrolled) {
        return new CourseResponse(
                c.getId(), c.getTitle(), c.getDescription(),
                c.getWeeks(), c.getLessons(), c.getPrice(),
                c.getImageUrl(), enrolledCount, enrolled, c.getCreatedAt()
        );
    }
}
