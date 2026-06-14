package com.pyqportal.paper;

import java.time.LocalDateTime;
import java.util.UUID;

public record PaperResponse(
        UUID id,
        String title,
        String subject,
        String branch,
        Integer year,
        String examType,
        String fileUrl,
        int downloadCount,
        String uploadedBy,
        LocalDateTime createdAt
) {
    public static PaperResponse from(Paper paper) {
        return new PaperResponse(
                paper.getId(),
                paper.getTitle(),
                paper.getSubject(),
                paper.getBranch().name(),
                paper.getYear(),
                paper.getExamType().name(),
                paper.getFileUrl(),
                paper.getDownloadCount(),
                paper.getUploadedBy() != null ? paper.getUploadedBy().getName() : null,
                paper.getCreatedAt()
        );
    }
}
