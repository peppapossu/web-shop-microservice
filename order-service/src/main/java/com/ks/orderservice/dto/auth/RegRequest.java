package com.ks.orderservice.dto.auth;


import jakarta.validation.constraints.NotNull;

public record RegRequest(
        @NotNull(message = "username is required")
        String username,

        @NotNull(message = "password is required")
        String password) {
}
