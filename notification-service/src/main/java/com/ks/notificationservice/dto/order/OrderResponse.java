package com.ks.notificationservice.dto.order;

import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        Long customerId,
        List<ItemResponse> items
) {
}
