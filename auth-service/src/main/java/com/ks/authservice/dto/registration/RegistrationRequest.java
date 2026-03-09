package com.ks.authservice.dto.registration;


import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull(message = "username is required")
        String username,

        @NotNull(message = "password is required")
        String password) {
}
