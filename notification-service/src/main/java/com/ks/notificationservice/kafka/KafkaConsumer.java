package com.ks.notificationservice.kafka;

import com.ks.avro.order.OrderCreatedEvent;
import com.ks.notificationservice.mapper.AvroMapper;
import com.ks.notificationservice.repository.InboxRepository;
import com.ks.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final static String TOPIC = "OrderCreated";

    private final OrderService orderService;
    private final AvroMapper avroMapper;
    private final InboxRepository inboxRepository;

    @KafkaListener(topics = TOPIC, groupId = "order-id", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void listen(
            @Payload byte[] payload,
            @Header(KafkaHeaders.RECEIVED_KEY) UUID aggregateId,
            Acknowledgment ack) {

        log.error("Message received");

        boolean firstTime = inboxRepository.tryInsert(aggregateId);

        if (!firstTime) {
            ack.acknowledge();
            return;
        }

        orderService.saveAllOrders(avroMapper.toOrderResponse(avroDeserialize(payload)));

        ack.acknowledge();
    }

    private static OrderCreatedEvent avroDeserialize(byte[] message) {
        DatumReader<OrderCreatedEvent> reader =
                new SpecificDatumReader<>(OrderCreatedEvent.class);

        BinaryDecoder decoder =
                DecoderFactory.get().binaryDecoder(message, null);

        OrderCreatedEvent result;
        try {
            result = reader.read(null, decoder);
        } catch (IOException e) {
            log.error("Error while deserialize message", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    @KafkaListener(topics = "order-created.DLT")
    public void handleDlt(ConsumerRecord<String, byte[]> record) {
        log.error("Message in DLT. key={}, headers={}",
                record.key(),
                record.headers()
        );
    }
}
