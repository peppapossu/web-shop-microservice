package com.ks.notificationservice.dto.order;

import java.math.BigDecimal;

public record ItemResponse(
        Long productId,
        BigDecimal price,
        BigDecimal totalPrice,
        Integer amount,
        Integer sale
) {
}
