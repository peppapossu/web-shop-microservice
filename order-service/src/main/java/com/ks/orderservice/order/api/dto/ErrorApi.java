package com.ks.orderservice.order.api.dto;

public record ErrorApi(
        String errorCode,
        String errorDescription
) {
}
