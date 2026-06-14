package com.pyqportal.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * JSON-serializable wrapper for Spring Data Page — avoids Redis serialization issues
 * that arise with PageImpl (no no-arg constructor).
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
