package com.ks.orderservice.service;


import com.ks.orderservice.dto.order.CreateOrderRequest;
import com.ks.orderservice.domain.order.Order;

public interface OrderService {

    Order create(CreateOrderRequest createOrderRequest);
}
