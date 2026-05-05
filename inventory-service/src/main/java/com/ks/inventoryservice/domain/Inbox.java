package com.ks.inventoryservice.domain;


import com.ks.items.v1.ReserveItemResponse;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.List;
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
    UUID idempotentKey;

    @Enumerated(EnumType.STRING)
    Status status;

    @Type(JsonType.class)
    @Column(name = "saved_result" ,columnDefinition = "jsonb")
    List<ReserveItemResponse> savedResult;

    @CreationTimestamp
    Timestamp createdAt;
}
