package com.ks.orderservice.dto.order.item;

import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        String name,
        Integer quantity,
        BigDecimal price,
        Integer discount,
        BigDecimal totalPrice
) {
}
