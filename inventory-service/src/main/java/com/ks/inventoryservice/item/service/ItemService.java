package com.ks.inventoryservice.item.service;

import com.ks.inventoryservice.item.api.dto.PagedResponse;
import com.ks.inventoryservice.item.api.dto.item.ItemResponse;
import com.ks.inventoryservice.exception.ItemNotFoundException;
import com.ks.inventoryservice.item.api.mapper.ItemApiMapper;
import com.ks.inventoryservice.item.domain.Item;
import com.ks.inventoryservice.transport.grpc.mapper.ItemProtoMapper;
import com.ks.inventoryservice.item.infrastructure.persistence.repository.ItemRepository;
import com.ks.items.v1.Availability;
import com.ks.items.v1.ReserveItemRequest;
import com.ks.items.v1.ReserveItemResponse;
import com.ks.items.v1.ReserveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemApiMapper itemApiMapper;
    private final ItemProtoMapper itemProtoMapper;

    @Transactional(readOnly = true)
    public PagedResponse<ItemResponse> findAll(Pageable pageable) {
        return  itemApiMapper.getPagedResponse(itemRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public ItemResponse findById(Long id) {
        return itemApiMapper.toItemResponse(itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }

    public ItemResponse save(Item item) {
        return itemApiMapper.toItemResponse(itemRepository.save(item));
    }

    public ItemResponse update(Long id, Item item) {
        Item savedItem = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)
                );

        savedItem.setName(item.getName());
        savedItem.setPrice(item.getPrice());
        savedItem.setStock(item.getStock());
        savedItem.setDiscount(item.getDiscount());

        return itemApiMapper.toItemResponse(itemRepository.save(savedItem));
    }

    public void delete(Long itemId) {
        itemRepository.deleteById(itemId);
    }


    public List<ReserveItemResponse> checkAndReserve(ReserveRequest request) {
        List<ReserveItemResponse> resultList = new ArrayList<>();

        for (ReserveItemRequest itemRequest : request.getItemsList()) {
            int result = itemRepository.checkAndReserve(itemRequest.getId(), itemRequest.getQuantity());

            Item item = itemRepository.findById(itemRequest.getId())
                    .orElseThrow(() -> new ItemNotFoundException(itemRequest.getId()));

            boolean reserved = result > 0;

            if (reserved) {
                resultList.add(mapResult(item, Availability.AVAILABLE, itemRequest.getQuantity()));
            } else {
                resultList.add(mapResult(item, Availability.UNAVAILABLE, 0));
            }
        }
        return resultList;
    }


    private ReserveItemResponse mapResult(Item item, Availability checkStatus, int reserved) {
        return ReserveItemResponse.newBuilder()
                .setId(item.getId())
                .setName(item.getName())
                .setAvailability(checkStatus)
                .setDiscount(item.getDiscount())
                .setPrice(itemProtoMapper.bigDecimalToMoney(item.getPrice()))
                .setReserved(reserved)
                .build();
    }
}
