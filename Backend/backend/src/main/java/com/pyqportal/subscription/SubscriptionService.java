package com.pyqportal.subscription;

import com.pyqportal.exception.ResourceNotFoundException;
import com.pyqportal.user.User;
import com.pyqportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public SubscriptionResponse subscribe(String subject, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Subscription subscription = Subscription.builder()
                .user(user)
                .subject(subject)
                .build();

        return SubscriptionResponse.from(subscriptionRepository.save(subscription));
    }

    @Transactional
    public void unsubscribe(UUID id, String userEmail) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found: " + id));

        if (!subscription.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You can only delete your own subscriptions");
        }

        subscriptionRepository.delete(subscription);
        log.info("Unsubscribed {} from subject={}", userEmail, subscription.getSubject());
    }

    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getMySubscriptions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        return subscriptionRepository.findByUser(user)
                .stream().map(SubscriptionResponse::from).toList();
    }
}
