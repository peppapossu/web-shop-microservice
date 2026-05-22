package com.ks.inventoryservice.item.api;

import com.ks.inventoryservice.item.api.dto.PagedResponse;
import com.ks.inventoryservice.item.api.mapper.ItemApiMapper;
import com.ks.inventoryservice.item.api.dto.item.CreateItemRequest;
import com.ks.inventoryservice.item.api.dto.item.ItemResponse;
import com.ks.inventoryservice.item.api.dto.item.UpdateItemRequest;
import com.ks.inventoryservice.item.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemApiMapper itemApiMapper;

    @GetMapping
    public ResponseEntity<PagedResponse<ItemResponse>> getAllItems(Pageable pageable) {
        return ResponseEntity.ok().body(
                itemService.findAll(pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok().body(
                itemService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Void> createItem(@Valid @RequestBody CreateItemRequest createItemRequest) {
        ItemResponse savedItem = itemService.save(itemApiMapper.toItem(createItemRequest));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedItem.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateItem(@PathVariable Long id, @Valid @RequestBody UpdateItemRequest updateItemRequest) {
        itemService.update(id, itemApiMapper.toItem(updateItemRequest));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
