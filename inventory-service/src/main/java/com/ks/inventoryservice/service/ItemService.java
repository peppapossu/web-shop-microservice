package com.ks.inventoryservice.service;

import com.ks.inventoryservice.dto.item.ItemResponse;
import com.ks.inventoryservice.domain.Item;
import com.ks.items.v1.ReserveItemResponse;
import com.ks.items.v1.ReserveRequest;

import java.util.List;

public interface ItemService {

    List<ItemResponse> findAll();

    ItemResponse findById(Long itemId);

    ItemResponse save(Item item);

    ItemResponse update(Long id, Item item);

    void delete(Long itemId);

    List<ReserveItemResponse> checkAndReserve(ReserveRequest request);
}
