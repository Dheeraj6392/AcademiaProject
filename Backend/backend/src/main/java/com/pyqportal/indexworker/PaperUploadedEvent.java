package com.pyqportal.indexworker;

/**
 * Kafka event published after a paper is successfully uploaded.
 * Includes fileUrl so the consumer can download the PDF without knowing the Cloudinary cloud name.
 */
public record PaperUploadedEvent(
        String paperId,
        String cloudinaryPublicId,
        String fileUrl,
        String title,
        String subject,
        String branch,
        Integer year,
        String examType
) {}
