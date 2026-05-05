package com.ks.orderservice.facade;

import com.ks.orderservice.dto.order.CreateOrderRequest;
import com.ks.orderservice.dto.order.CreateOrderResponse;

public interface OrderFacadeService {
    CreateOrderResponse create(CreateOrderRequest  createOrderRequest);
}
