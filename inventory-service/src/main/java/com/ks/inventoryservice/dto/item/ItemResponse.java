package com.ks.inventoryservice.dto.item;

import java.math.BigDecimal;

public record ItemResponse(
        Long id,
        String name,
        Integer quantity,
        BigDecimal price,
        Integer discount
) {
}
