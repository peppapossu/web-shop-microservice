package com.ks.orderservice.service;

import com.ks.avro.order.OrderCreatedEvent;

public interface OutboxService {

    void saveOrderEvent(OrderCreatedEvent orderEvent);
}
