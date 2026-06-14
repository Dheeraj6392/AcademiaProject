package com.pyqportal.auth;

public record AuthResponse(
        String token,
        String userId,
        String email,
        String role
) {}
