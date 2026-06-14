package com.pyqportal.subscription;

import java.time.LocalDateTime;
import java.util.UUID;

/** Avoids lazy-loading User inside Subscription when serializing to JSON */
public record SubscriptionResponse(UUID id, String subject, LocalDateTime createdAt) {
    public static SubscriptionResponse from(Subscription s) {
        return new SubscriptionResponse(s.getId(), s.getSubject(), s.getCreatedAt());
    }
}
