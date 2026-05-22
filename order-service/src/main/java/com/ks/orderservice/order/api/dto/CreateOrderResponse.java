package com.ks.orderservice.order.api.dto;

import com.ks.orderservice.order.api.dto.item.ItemResponse;

import java.util.List;
import java.util.UUID;

public record CreateOrderResponse(
        UUID orderUUID,
        UUID customerUUID,
        List<ItemResponse> items
) {
}
