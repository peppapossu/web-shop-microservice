package com.ks.orderservice.order.api.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public record ItemRequest(

        @NotBlank
        Long id,

        @Positive
        Integer quantity

) {
}
