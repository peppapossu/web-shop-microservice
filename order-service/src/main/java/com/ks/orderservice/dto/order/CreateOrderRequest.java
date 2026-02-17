package com.ks.orderservice.dto.order;

import com.ks.orderservice.dto.order.item.ItemRequest;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

public record CreateOrderRequest(
        @NotBlank
        Long customerId,

        @NotBlank
        Set<ItemRequest> items
) {
}
