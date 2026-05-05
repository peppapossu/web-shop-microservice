package com.ks.inventoryservice.service.impl;

import com.ks.inventoryservice.domain.Inbox;
import com.ks.inventoryservice.domain.Status;
import com.ks.inventoryservice.dto.item.ItemResponse;
import com.ks.inventoryservice.exception.ItemNotFoundException;
import com.ks.inventoryservice.domain.Item;
import com.ks.inventoryservice.mapper.ItemMapper;
import com.ks.inventoryservice.mapper.ProtoMapper;
import com.ks.inventoryservice.repository.InboxRepository;
import com.ks.inventoryservice.repository.ItemRepository;
import com.ks.inventoryservice.service.ItemService;
import com.ks.items.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ProtoMapper protoMapper;
    private final InboxRepository inboxRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ItemResponse> findAll() {
        return itemMapper.toItemsResponse(itemRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public ItemResponse findById(Long id) {
        return itemMapper.toItemResponse(itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)));
    }

    @Override
    public ItemResponse save(Item item) {
        return itemMapper.toItemResponse(itemRepository.save(item));
    }

    @Override
    public ItemResponse update(Long id, Item item) {
        Item savedItem = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)
                );

        savedItem.setName(item.getName());
        savedItem.setPrice(item.getPrice());
        savedItem.setStock(item.getStock());
        savedItem.setDiscount(item.getDiscount());

        return itemMapper.toItemResponse(itemRepository.save(savedItem));
    }

    @Override
    public void delete(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public List<ReserveItemResponse> checkAndReserve(ReserveRequest request) {

        UUID idempotentKey = UUID.fromString(request.getId());

        Inbox inbox = Inbox.builder()
                .idempotentKey(idempotentKey)
                .status(Status.IN_PROGRESS)
                .build();

        try {
            inboxRepository.saveAndFlush(inbox);
        } catch (DuplicateKeyException ex) {
            Inbox inboxSaved = inboxRepository.findByIdempotentKey(idempotentKey);
            if (inboxSaved.getStatus() == Status.SUCCEEDED) {
                return inboxSaved.getSavedResult();
            } else if (inboxSaved.getStatus() == Status.IN_PROGRESS) {
                throw new RetraybleExeption("Method status IN_PROGRESS");
            }
        }

        List<ReserveItemResponse> resultList = new ArrayList<>();

        try {
            for (ReserveItemRequest itemRequest : request.getItemsList()) {
                int result = itemRepository.checkAndReserve(itemRequest.getId(), itemRequest.getQuantity());

                Item item = itemRepository.findById(itemRequest.getId())
                        .orElseThrow(() -> new ItemNotFoundException(itemRequest.getId()));

                if (result == 1) {
                    resultList.add(mapResult(item, Availability.AVAILABLE, itemRequest.getQuantity()));
                } else {
                    resultList.add(mapResult(item, Availability.UNAVAILABLE, 0));
                }
            }
            inbox.setSavedResult(resultList);
            inbox.setStatus(Status.SUCCEEDED);
        } catch (Exception ex) {
            inbox.setStatus(Status.FAILED);
            throw ex;
        }

        inboxRepository.saveAndFlush(inbox);
        return resultList;
    }

    private ReserveItemResponse mapResult(Item item, Availability checkStatus, int reserved) {
        return ReserveItemResponse.newBuilder()
                .setId(item.getId())
                .setName(item.getName())
                .setAvailability(checkStatus)
                .setDiscount(item.getDiscount())
                .setPrice(protoMapper.bigDecimalToMoney(item.getPrice()))
                .setDegraded(false)
                .setReserved(reserved)
                .build();
    }


}
