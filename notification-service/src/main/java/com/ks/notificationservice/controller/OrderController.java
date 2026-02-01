package com.ks.notificationservice.controller;

import com.ks.notificationservice.dto.PagedResponse;
import com.ks.notificationservice.dto.order.OrderResponse;
import com.ks.notificationservice.mapper.OrderMapper;
import com.ks.notificationservice.mapper.PagedResponseMapper;
import com.ks.notificationservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping("/all")
    public ResponseEntity<PagedResponse<OrderResponse>> getAllOrders(
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(PagedResponseMapper.map(
                orderMapper.toOrderResponsePage(
                        orderService.getAllOrders(pageable))));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<PagedResponse<OrderResponse>> getAllOrdersByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(PagedResponseMapper.map(
                orderMapper.toOrderResponsePage(
                        orderService.getOrdersByCustomerId(userId, pageable))));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<PagedResponse<OrderResponse>> getAllOrdersByOrderId(
            @PathVariable Long orderId,
            @PageableDefault(size = 20, sort = "orderId", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(PagedResponseMapper.map(
                orderMapper.toOrderResponsePage(
                        orderService.getOrdersByOrderId(orderId, pageable))));
    }
}
