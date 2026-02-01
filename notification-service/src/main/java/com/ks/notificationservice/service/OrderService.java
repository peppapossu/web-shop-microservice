package com.ks.notificationservice.service;

import com.ks.notificationservice.dto.order.OrderResponse;
import com.ks.notificationservice.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface OrderService {

    void saveAllOrders(OrderResponse orderResponse);

    Page<Order> getOrdersByCustomerId(Long id, Pageable pageable);

    Page<Order> getOrdersByOrderId(Long id, Pageable pageable);

    Page<Order> getAllOrders(Pageable pageable);

    boolean isOrderExists(Long orderId);
}
