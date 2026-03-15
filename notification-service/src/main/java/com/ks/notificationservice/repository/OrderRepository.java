package com.ks.notificationservice.repository;

import com.ks.notificationservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(Long id, Pageable pageable);

    Page<Order> findByOrderId(UUID orderId, Pageable pageable);

    boolean existsByOrderId(UUID orderId);
}
