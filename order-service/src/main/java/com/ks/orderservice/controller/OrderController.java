package com.ks.orderservice.controller;

import com.ks.avro.order.OrderCreatedEvent;
import com.ks.orderservice.dto.ErrorApi;
import com.ks.orderservice.dto.order.CreateOrderRequest;
import com.ks.orderservice.kafka.KafkaProducer;
import com.ks.orderservice.mapper.OrderMapper;
import com.ks.orderservice.service.OrderService;
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

    private final OrderService orderService;
//    private final KafkaProducer kafkaProducer;
    private final OrderMapper orderMapper;

    private final static String ORDER = "order";

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest) {

        OrderCreatedEvent orderCreatedEvent = orderService.checkAndReserveStock(createOrderRequest);
        orderService.saveOrderToOutbox(orderCreatedEvent);
//        if (orderCreatedEvent.getItems().isEmpty()) {
//            return ResponseEntity.internalServerError().body(
//                    new ErrorApi(
//                            "500","Error get the item from the stock, try again later"));
//        }
//
//        kafkaProducer.send(
//                ORDER,
//                createOrderRequest.orderId().toString(),
//                orderCreatedEvent);
//
//        log.error("OrderService successfully sent message to Kafka: '{}'", createOrderRequest);
        return ResponseEntity.ok(orderMapper.toCreateOrderResponse(orderCreatedEvent));
    }
}
