package com.ks.orderservice.dto.order;

import com.ks.orderservice.dto.order.item.ItemResponse;

import java.util.List;

public record CreateOrderResponse(
        Long orderId,
        Long customerId,
        List<ItemResponse> items
) {
}
