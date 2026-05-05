package com.ks.orderservice.facade.impl;

import com.ks.orderservice.dto.order.CreateOrderRequest;
import com.ks.orderservice.dto.order.CreateOrderResponse;
import com.ks.orderservice.domain.order.Order;
import com.ks.orderservice.dto.order.mapper.OrderDtoMapper;
import com.ks.orderservice.facade.OrderFacadeService;
import com.ks.orderservice.service.OrderService;
import com.ks.orderservice.infrastructure.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacadeServiceImpl implements OrderFacadeService {

    private final OrderService orderService;
    private final OutboxService outboxService;
    private final OrderDtoMapper orderMapper;

    @Transactional
    public CreateOrderResponse create(CreateOrderRequest createOrderRequest) {

        Order order = orderService.create(createOrderRequest);
        outboxService.saveOrderEvent(order);

        return orderMapper.toCreateOrderResponse(order) ;
    }
}
