package com.ks.orderservice.entity.outbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    private UUID id;

    private String aggregateType;

    private Long aggregateId;

    private String eventType;

    @Lob
    private byte[] payload;

    private String payloadType;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status;

    private int retryCount;

    private Instant createdAt;
    private Instant sentAt;
}
