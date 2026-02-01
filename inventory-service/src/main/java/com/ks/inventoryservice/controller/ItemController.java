package com.ks.inventoryservice.controller;

import com.ks.inventoryservice.mapper.ItemMapper;
import com.ks.inventoryservice.dto.item.CreateItemRequest;
import com.ks.inventoryservice.dto.item.ItemResponse;
import com.ks.inventoryservice.dto.item.UpdateItemRequest;
import com.ks.inventoryservice.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getItems() {
        return ResponseEntity.ok().body(
                itemMapper.itemsToItemsResponse(
                        itemService.getAllItems()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                itemMapper.itemToItemResponse(
                        itemService.getItemById(id)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody CreateItemRequest createItemRequest) {
        itemService.saveItem(
                itemMapper.createItemRequestToItem(createItemRequest)
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody UpdateItemRequest updateItemRequest) {
        itemService.updateItem(
                itemMapper.updateItemRequestToItem(updateItemRequest)
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
}
