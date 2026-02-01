package com.ks.inventoryservice.service;

import com.ks.inventoryservice.entity.Item;

import java.util.List;

public interface ItemService {

    List<Item> getAllItems();

    Item getItemById(Long itemId);

    Item saveItem(Item item);

    Item updateItem(Item item);

    void deleteItem(Long itemId);
}
