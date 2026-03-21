package com.ks.orderservice.service.impl;

import com.ks.avro.order.OrderCreatedEvent;
import com.ks.orderservice.entity.id.UUIDGenerator;
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
    private final static String ORDER = "Order";
    private final int SCHEMA_VERSION = 1;

    private final OutboxRepository repository;
    private final UUIDGenerator uuidGenerator;

    @Transactional
    public void saveOrderEvent(OrderCreatedEvent orderEvent) {

        OutboxEvent event = OutboxEvent.builder()
                .eventId(uuidGenerator.next())
                .aggregateType(ORDER)
                .aggregateId(uuidGenerator.next())
                .eventType(ORDER_CREATED)
                .payload(AvroSerializer.serialize(orderEvent))
                .createdAt(Instant.now())
                .schemaVersion(SCHEMA_VERSION)
                .build();

        repository.save(event);
    }
}
