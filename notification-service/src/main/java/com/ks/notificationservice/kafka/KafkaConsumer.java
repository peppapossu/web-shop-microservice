package com.ks.notificationservice.kafka;

import com.ks.notificationservice.mapper.AvroDeserializer;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final static String TOPIC = "OrderCreated";
    private final static String ORDER_ID = "order-id";
    private final static String KLCF = "kafkaListenerContainerFactory";

    private final OrderService orderService;
    private final AvroMapper avroMapper;
    private final InboxRepository inboxRepository;
    private final AvroDeserializer avroDeserializer;

    @KafkaListener(topics = TOPIC, groupId = ORDER_ID, containerFactory = KLCF)
    @Transactional
    public void listen(
            @Payload byte[] payload,
            @Header(KafkaHeaders.RECEIVED_KEY) UUID aggregateId,
            Acknowledgment ack) {

        boolean firstTime = inboxRepository.tryInsert(aggregateId);

        if (!firstTime) {
            ack.acknowledge();
            return;
        }

        orderService.saveAllOrders(
                avroMapper.toOrderResponse(
                        avroDeserializer.deserialize(payload)));

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
