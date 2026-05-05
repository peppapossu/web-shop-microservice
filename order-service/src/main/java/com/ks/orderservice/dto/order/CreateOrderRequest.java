package com.ks.orderservice.dto.order;

import com.ks.orderservice.dto.order.item.ItemRequest;
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
