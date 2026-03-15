package com.ks.orderservice.dto.order;

import com.ks.orderservice.dto.order.item.ItemResponse;

import java.util.List;
import java.util.UUID;

public record CreateOrderResponse(
        UUID orderId,
        Long customerId,
        List<ItemResponse> items
) {
}
