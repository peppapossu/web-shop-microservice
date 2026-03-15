package com.ks.orderservice.service.impl;

import com.ks.avro.order.OrderCreatedEvent;
import com.ks.orderservice.entity.id.IdGenerator;
import com.ks.orderservice.entity.outbox.OutboxEvent;
import com.ks.orderservice.mapper.AvroSerializer;
import com.ks.orderservice.repository.OutboxRepository;
import com.ks.orderservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxServiceImpl implements OutboxService {

    private final static String ORDER_CREATED = "OrderCreated";

    private final OutboxRepository repository;
    private final IdGenerator idGenerator;

    @Transactional
    public void saveOrderEvent(OrderCreatedEvent orderEvent) {


        OutboxEvent event = OutboxEvent.builder()
                .eventId(idGenerator.next())
                .aggregateType("Order")
                .aggregateId(idGenerator.next())
                .eventType(ORDER_CREATED)
                .payload(AvroSerializer.serialize(orderEvent))
                .createdAt(Instant.now())
                .build();

        repository.save(event);
    }
}
