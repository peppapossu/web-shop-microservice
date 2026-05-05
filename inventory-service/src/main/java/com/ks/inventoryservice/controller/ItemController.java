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
    public ResponseEntity<List<ItemResponse>> getAllItems() {
        return ResponseEntity.ok().body(
                        itemService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                        itemService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<?> addNewItem(@RequestBody CreateItemRequest createItemRequest) {
        itemService.save(itemMapper.toItem(createItemRequest));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody UpdateItemRequest updateItemRequest) {
        itemService.update(id, itemMapper.toItem(updateItemRequest));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
