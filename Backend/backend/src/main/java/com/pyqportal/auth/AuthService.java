package com.pyqportal.auth;

import com.pyqportal.exception.ResourceNotFoundException;
import com.pyqportal.user.Role;
import com.pyqportal.user.User;
import com.pyqportal.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ALLOWED_DOMAIN = "@iiita.ac.in";

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    /** Login — fails if user is not registered, or if name does not match. */
    public AuthResponse login(AuthRequest request) {
        validateDomain(request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No account found for " + request.email() + ". Please sign up first."));

        if (!user.getName().equalsIgnoreCase(request.name().trim())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Name does not match our records. Please check and try again.");
        }

        return buildResponse(user);
    }

    /** Register — fails if email is already registered. */
    public AuthResponse register(AuthRequest request) {
        validateDomain(request.email());

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "An account already exists for " + request.email() + ". Please sign in instead.");
        }

        log.info("New registration for {}", request.email());
        User user = userRepository.save(User.builder()
                .email(request.email())
                .name(request.name())
                .role(Role.STUDENT)
                .build());

        return buildResponse(user);
    }

    private void validateDomain(String email) {
        if (!email.endsWith(ALLOWED_DOMAIN)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only " + ALLOWED_DOMAIN + " email addresses are allowed");
        }
    }

    private AuthResponse buildResponse(User user) {
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId().toString(), user.getEmail(), user.getRole().name());
    }
}
