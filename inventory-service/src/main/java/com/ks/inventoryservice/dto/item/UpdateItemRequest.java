package com.ks.inventoryservice.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateItemRequest(
        Long id,

        @NotBlank
        String name,

        @Positive
        @NotBlank
        Integer quantity,

        @Positive
        @NotBlank
        BigDecimal price,

        @Positive
        @NotBlank
        Integer discount
) {
}
