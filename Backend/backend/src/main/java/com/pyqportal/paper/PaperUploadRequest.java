package com.pyqportal.paper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Bound via @ModelAttribute from multipart/form-data — must be a mutable class (not a record).
 */
@Data
public class PaperUploadRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String subject;

    @NotNull
    private Branch branch;

    @NotNull
    private Integer year;

    @NotNull
    private ExamType examType;
}
