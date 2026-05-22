package com.ks.orderservice.order.api.dto;

import com.ks.orderservice.order.api.dto.item.ItemRequest;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
        @NotBlank
        UUID customerUUID,

        @NotBlank
        List<ItemRequest> items
) {
}
