package com.ks.notificationservice.kafka;

import com.ks.avro.order.OrderCreatedEvent;
import com.ks.notificationservice.mapper.AvroMapper;
import com.ks.notificationservice.repository.InboxRepository;
import com.ks.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrderService orderService;
    private final AvroMapper avroMapper;
    private final InboxRepository inboxRepository;

    @KafkaListener(topics = "order", groupId = "order-id", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void listen(
            @Payload OrderCreatedEvent message,
            @Header(KafkaHeaders.RECEIVED_KEY) UUID eventId,
            Acknowledgment ack) {

        boolean firstTime = inboxRepository.tryInsert(eventId);

        if (!firstTime) {
            ack.acknowledge();
            return;
        }

        orderService.saveAllOrders(avroMapper.toOrderResponse(message)); // здесь прям другой обьект TODO

        ack.acknowledge();
    }

    @KafkaListener(topics = "order-created.DLT")
    public void handleDlt(ConsumerRecord<String, byte[]> record) {
        log.error("Message in DLT. key={}, headers={}",
                record.key(),
                record.headers()
        );
    }
}
