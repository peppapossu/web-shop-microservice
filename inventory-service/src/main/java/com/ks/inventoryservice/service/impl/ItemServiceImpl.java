package com.ks.inventoryservice.service.impl;

import com.ks.inventoryservice.exception.ItemNotFoundException;
import com.ks.inventoryservice.entity.Item;
import com.ks.inventoryservice.repository.ItemRepository;
import com.ks.inventoryservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        Item savedItem = getItemById(item.getId());

        savedItem.setName(item.getName());
        savedItem.setPrice(item.getPrice());
        savedItem.setQuantity(item.getQuantity());
        savedItem.setDiscount(item.getDiscount());

        return itemRepository.save(savedItem);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }


}
