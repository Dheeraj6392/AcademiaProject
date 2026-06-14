package com.pyqportal.subscription;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
@Tag(name = "Subscriptions", description = "Subject subscription management")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @Operation(summary = "Subscribe to a subject")
    public ResponseEntity<SubscriptionResponse> subscribe(
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(subscriptionService.subscribe(body.get("subject"), email));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Unsubscribe (owner only)")
    public ResponseEntity<Void> unsubscribe(@PathVariable UUID id, Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        subscriptionService.unsubscribe(id, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get all subscriptions for the logged-in user")
    public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        return ResponseEntity.ok(subscriptionService.getMySubscriptions(email));
    }
}
