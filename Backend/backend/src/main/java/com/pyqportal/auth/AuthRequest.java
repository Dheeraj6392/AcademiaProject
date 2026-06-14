package com.pyqportal.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank @Email String email,
        @NotBlank String name,
        @NotBlank String googleToken
) {}
