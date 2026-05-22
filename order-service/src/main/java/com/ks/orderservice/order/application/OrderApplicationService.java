package com.ks.orderservice.order.application;

import com.ks.orderservice.order.api.dto.CreateOrderRequest;
import com.ks.orderservice.order.api.dto.CreateOrderResponse;
import com.ks.orderservice.order.api.mapper.OrderApiMapper;
import com.ks.orderservice.order.entity.Order;
import com.ks.orderservice.order.infrastructure.outbox.service.OutboxService;
import com.ks.orderservice.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final OrderService orderService;
    private final OutboxService outboxService;
    private final OrderApiMapper orderApiMapper;

    @Transactional
    public CreateOrderResponse create(CreateOrderRequest createOrderRequest) {

        Order order = orderService.create(createOrderRequest);
        outboxService.saveOrderEvent(order);

        return orderApiMapper.toCreateOrderResponse(order) ;
    }
}
