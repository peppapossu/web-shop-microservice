package com.ks.orderservice.service.impl;

import com.ks.avro.order.Item;
import com.ks.avro.order.OrderCreatedEvent;
import com.ks.common.proto.ProductResponse;
import com.ks.orderservice.dto.order.CreateOrderRequest;
import com.ks.orderservice.dto.order.item.ItemRequest;
import com.ks.orderservice.entity.id.IdGenerator;
import com.ks.orderservice.grpc.ProductClient;
import com.ks.orderservice.mapper.AvroMapper;
import com.ks.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ks.common.proto.Availability.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductClient productClient;
    private final AvroMapper itemMapper;
    private final IdGenerator idGenerator;

    public OrderCreatedEvent checkAndReserveStock(CreateOrderRequest request) {

        List<Item> items = new ArrayList<>();

        for (ItemRequest item : request.items()) {
            ProductResponse itemResponse = productClient.checkAvailability(item.itemId());
            if (itemResponse.getAvailability() == AVAILABLE) {
                items.add(itemMapper.toItemAvro(itemResponse));
            }
        }

        return OrderCreatedEvent.newBuilder()
                .setCustomerId(request.customerId())
                .setOrderId(idGenerator.next().toString())
                .setItems(items)
                .build();
    }
}
