package com.ks.notificationservice.dto.order;

import java.util.List;

public record OrderResponse(
        Long orderId,
        Long customerId,
        List<ItemResponse> items
) {
}
