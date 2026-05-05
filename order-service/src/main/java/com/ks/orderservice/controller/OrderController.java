package com.ks.orderservice.controller;

import com.ks.orderservice.dto.order.CreateOrderRequest;
import com.ks.orderservice.dto.order.CreateOrderResponse;
import com.ks.orderservice.facade.OrderFacadeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderFacadeService orderFacadeService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        log.debug("OrderController -> creating order from createOrderRequest = {}", createOrderRequest);
        return ResponseEntity.ok(orderFacadeService.create(createOrderRequest));
    }
}
