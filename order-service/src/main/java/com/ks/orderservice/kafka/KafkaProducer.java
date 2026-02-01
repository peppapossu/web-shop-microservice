package com.ks.orderservice.kafka;

import com.ks.avro.order.OrderCreatedEvent;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @RateLimiter(name = "kafkaRate")
    @Bulkhead(name = "kafkaProducer", type = Bulkhead.Type.SEMAPHORE)
    public void send(String topic, String key, OrderCreatedEvent message) {

            kafkaTemplate.send(topic, key, message);
            log.info("OrderService successfully sent message to Kafka: '{}'", message);
    }

}
