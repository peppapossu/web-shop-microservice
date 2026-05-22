package com.ks.orderservice.gateway.inventory;

import com.ks.orderservice.order.api.dto.item.ItemRequest;
import com.ks.orderservice.gateway.inventory.grpc.dto.ReservationResult;

import java.util.List;

public interface InventoryGateway {
    ReservationResult reserve(String requestId, List<ItemRequest> itemsRequest);
}
