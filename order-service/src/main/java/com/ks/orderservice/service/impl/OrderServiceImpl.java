package com.ks.orderservice.service.impl;

import com.ks.orderservice.common.id.IdGenerator;
import com.ks.orderservice.dto.order.CreateOrderRequest;
import com.ks.orderservice.domain.order.Order;
import com.ks.orderservice.domain.order.Status;
import com.ks.orderservice.exception.InventoryServiceNotAvailable;
import com.ks.orderservice.exception.NotEnoughStockException;
import com.ks.orderservice.gateway.inventory.InventoryGateway;
import com.ks.orderservice.gateway.inventory.grpc.mapper.EntityMapper;
import com.ks.orderservice.repository.OrderRepository;
import com.ks.orderservice.service.OrderService;
import com.ks.orderservice.service.dto.ReservationItemResult;
import com.ks.orderservice.service.dto.ReservationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final InventoryGateway inventoryGateway;
    private final IdGenerator uuidGenerator;
    private final OrderRepository orderRepository;
    private final EntityMapper entityMapper;


    @Transactional
    public Order create(CreateOrderRequest request) {

        String requestId = uuidGenerator.next().toString();

        ReservationResult response = inventoryGateway.reserve(
                requestId,
                request.items()
        );

        if (response.resultList().isEmpty()) {
            throw new InventoryServiceNotAvailable("Inventory service not available");
        }

        int attempts = 3;

        while (response.status() == ReservationResult.Status.FAILURE && attempts > 0 ) {
            attempts--;
            response = inventoryGateway.reserve(
                    requestId,
                    request.items());
        }

        boolean allReserved = response.resultList()
                .stream()
                .allMatch(r -> r.availability() == ReservationItemResult.Availability.AVAILABLE);

        if (!allReserved) {
            throw new NotEnoughStockException("Not enough at the stock for customer id: " + request.customerUUID().toString());
        }

        Order order = Order.builder()
                .orderUUID(uuidGenerator.next())
                .customerUUID(request.customerUUID())
                .items(entityMapper.toItems(response.resultList()))
                .status(Status.NEW)
                .totalPrice(getTotalPrice(response.resultList()))
                .build();

        return orderRepository.save(order);
    }

    private BigDecimal getTotalPrice(List<ReservationItemResult> reserveItemResponses) {
        return reserveItemResponses.stream()
                .map(e -> e.price()
                        .multiply(BigDecimal.valueOf(e.reserved())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
