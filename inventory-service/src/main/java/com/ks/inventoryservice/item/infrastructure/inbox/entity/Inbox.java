package com.ks.inventoryservice.item.infrastructure.inbox.entity;


import com.ks.inventoryservice.item.domain.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "inbox")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inbox {

    @Id
    @Column(name = "idempotent_key")
    private UUID idempotentKey;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "saved_result", columnDefinition = "bytea")
    private byte[] savedResult;

    @CreationTimestamp
    private Timestamp createdAt;
}
