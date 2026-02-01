package com.ks.orderservice.dto;

public record ErrorApi(
        String errorCode,
        String errorDescription
) {
}
