package com.ks.orderservice.order.infrastructure.outbox.service;

import com.ks.orderservice.common.id.IdGenerator;
import com.ks.orderservice.order.entity.Order;
import com.ks.orderservice.order.infrastructure.outbox.entity.OutboxOrder;
import com.ks.orderservice.order.infrastructure.outbox.mapper.OutboxAvroSerializer;
import com.ks.orderservice.order.infrastructure.outbox.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private static final String ORDER_CREATED = "OrderCreated";
    private static final String ORDER = "Order";
    private static final int SCHEMA_VERSION = 1;

    private final OutboxRepository outboxRepository;
    private final IdGenerator idGenerator;

    @Transactional
    public void saveOrderEvent(Order order) {

        OutboxOrder event = OutboxOrder.builder()
                .eventId(idGenerator.next())
                .aggregateType(ORDER)
                .aggregateId(idGenerator.next())
                .eventType(ORDER_CREATED)
                .payload(OutboxAvroSerializer.serialize(order))
                .createdAt(Instant.now())
                .schemaVersion(SCHEMA_VERSION)
                .build();

        outboxRepository.save(event);
    }
}
