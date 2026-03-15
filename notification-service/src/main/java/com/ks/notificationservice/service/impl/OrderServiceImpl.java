package com.ks.notificationservice.service.impl;

import com.ks.notificationservice.dto.order.ItemResponse;
import com.ks.notificationservice.dto.order.OrderResponse;
import com.ks.notificationservice.entity.Order;
import com.ks.notificationservice.repository.OrderRepository;
import com.ks.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public Page<Order> getOrdersByCustomerId(Long id, Pageable pageable) {
        return orderRepository.findByCustomerId(id, pageable);
    }

    public Page<Order> getOrdersByOrderId(UUID id, Pageable pageable) {
        return orderRepository.findByOrderId(id, pageable);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public boolean isOrderExists(UUID orderId) {
        return orderRepository.existsByOrderId(orderId);
    }

    @Transactional
    public void saveAllOrders(OrderResponse orderResponse) {
        for (ItemResponse item : orderResponse.items()){
            orderRepository.save(
                    Order.builder()
                    .orderId(orderResponse.orderId())
                    .customerId(orderResponse.customerId())
                    .productId(item.productId())
                    .price(item.price())
                    .sale(item.sale())
                    .quantity(item.amount())
                    .totalPrice(item.totalPrice())
                    .build());
        }
    }
}
