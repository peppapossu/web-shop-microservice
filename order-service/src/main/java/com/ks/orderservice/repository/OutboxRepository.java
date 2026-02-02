package com.ks.orderservice.repository;

import com.ks.orderservice.entity.outbox.OutboxEvent;
import com.ks.orderservice.entity.outbox.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findTop100ByStatusOrderByCreatedAt(OutboxStatus outboxStatus);
}
