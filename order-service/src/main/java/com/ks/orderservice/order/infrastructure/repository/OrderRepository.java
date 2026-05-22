package com.ks.orderservice.infrastructure.outbox.repository;

import com.ks.orderservice.infrastructure.outbox.entity.OutboxOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxOrder, UUID> {

}
