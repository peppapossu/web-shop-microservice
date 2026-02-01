package com.ks.notificationservice.kafka;

import com.ks.avro.order.OrderCreatedEvent;
import com.ks.notificationservice.mapper.AvroMapper;
import com.ks.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final OrderService orderService;
    private final AvroMapper avroMapper;

    @KafkaListener(topics = "order", groupId = "order-id", containerFactory = "kafkaListenerContainerFactory")
    public void listen(OrderCreatedEvent message, Acknowledgment ack) {

        if (!orderService.isOrderExists(message.getOrderId())) {
            orderService.saveAllOrders(avroMapper.toOrderResponse(message));
        } else {
            log.warn("Order with id {} already exists", message.getOrderId());
        }

        ack.acknowledge();
    }

    @KafkaListener(topics = "order-created.DLT")
    public void handleDlt(ConsumerRecord<String, byte[]> record) {
        log.error("💀 Message in DLT. key={}, headers={}",
                record.key(),
                record.headers()
        );
    }
}
