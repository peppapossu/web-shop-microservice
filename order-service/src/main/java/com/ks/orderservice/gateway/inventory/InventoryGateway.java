package com.ks.orderservice.gateway.inventory;

import com.ks.orderservice.dto.order.item.ItemRequest;
import com.ks.orderservice.service.dto.ReservationResult;

import java.util.List;

public interface InventoryGateway {
    ReservationResult reserve(String requestId, List<ItemRequest> itemsRequest);
}
