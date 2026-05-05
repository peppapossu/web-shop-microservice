package com.ks.orderservice.infrastructure.outbox.service;

import com.ks.orderservice.domain.order.Order;

public interface OutboxService {

    void saveOrderEvent(Order order);
}
