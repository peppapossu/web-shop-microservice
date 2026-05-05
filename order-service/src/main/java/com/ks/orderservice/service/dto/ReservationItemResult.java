package com.ks.orderservice.service.dto;


import java.math.BigDecimal;

public record ReservationItemResult(
        Long id,
        String name,
        Integer reserved,
        BigDecimal price,
        Integer discount,
        Availability availability,
        Boolean degraded
) {

    public enum Availability {
        AVAILABLE,
        UNAVAILABLE,
        UNKNOWN
    }
}