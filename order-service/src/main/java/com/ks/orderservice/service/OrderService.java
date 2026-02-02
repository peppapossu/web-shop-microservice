package com.ks.orderservice.service;


import com.ks.avro.order.OrderCreatedEvent;
import com.ks.orderservice.dto.order.CreateOrderRequest;

public interface OrderService {

    void saveOrderToOutbox(OrderCreatedEvent orderCreatedEvent);

    OrderCreatedEvent checkAndReserveStock (CreateOrderRequest createOrderRequest);
}
