package com.ks.orderservice.dto.customer;

public record CustomerResponse(
        Long id,
        String username,
        String email,
        String role
) {
}
