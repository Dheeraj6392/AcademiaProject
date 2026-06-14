package com.pyqportal.course;

import jakarta.validation.constraints.NotBlank;

public record CourseRequest(
        @NotBlank String title,
        String description,
        int weeks,
        int lessons,
        double price,
        String imageUrl
) {}
