package com.ks.orderservice.dto.order.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public record ItemRequest(

        @NotBlank
        Long id,

        @Positive
        Integer quantity

) {
}
