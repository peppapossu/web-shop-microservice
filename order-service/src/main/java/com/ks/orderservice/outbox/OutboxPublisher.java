package com.ks.orderservice.outbox;

import com.ks.orderservice.entity.outbox.OutboxEvent;
import com.ks.orderservice.entity.outbox.OutboxStatus;
import com.ks.orderservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @Scheduled(fixedDelay = 2000)
    public void publish() {

        List<OutboxEvent> events =
            outboxRepository.findTop100ByStatusOrderByCreatedAt(OutboxStatus.NEW);

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(
                    "order-created",
                    event.getAggregateId().toString(),
                    deserialize(event)
                );

                event.setStatus(OutboxStatus.SENT);
                event.setSentAt(Instant.now());

            } catch (Exception ex) {
                event.setRetryCount(event.getRetryCount() + 1);

                if (event.getRetryCount() > 10) {
                    event.setStatus(OutboxStatus.FAILED);
                }
            }
        }
    }

    private SpecificRecord deserialize(OutboxEvent event) {

        try {
            Class<?> clazz = Class.forName(event.getPayloadType());

            SpecificDatumReader<SpecificRecord> reader =
                    new SpecificDatumReader<>(
                            ((SpecificRecord) clazz.getDeclaredConstructor().newInstance())
                                    .getSchema()
                    );

            BinaryDecoder decoder =
                    DecoderFactory.get().binaryDecoder(event.getPayload(), null);

            return reader.read(null, decoder);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize Avro", e);
        }
    }

}
