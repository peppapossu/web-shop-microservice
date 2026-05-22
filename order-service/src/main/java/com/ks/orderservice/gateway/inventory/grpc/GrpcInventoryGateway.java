package com.ks.orderservice.gateway.inventory.grpc;


import com.ks.items.v1.ItemsInventoryServiceGrpc;
import com.ks.items.v1.ReserveRequest;
import com.ks.orderservice.gateway.inventory.grpc.mapper.InventoryProtoMapper;
import com.ks.orderservice.gateway.inventory.grpc.mapper.InventoryResultProto;
import com.ks.orderservice.order.api.dto.item.ItemRequest;
import com.ks.orderservice.gateway.inventory.InventoryGateway;
import com.ks.orderservice.gateway.inventory.grpc.dto.ReservationItemResult;
import com.ks.orderservice.gateway.inventory.grpc.dto.ReservationResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Profile("!dev")
@RequiredArgsConstructor
public class GrpcInventoryGateway implements InventoryGateway {

    private final InventoryProtoMapper inventoryProtoMapper;
    private final InventoryResultProto inventoryResultProto;

    @GrpcClient("inventory-service")
    private ItemsInventoryServiceGrpc.ItemsInventoryServiceBlockingStub stub;

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
    @Retry(name = "inventory")
    public ReservationResult reserve(String requestId, List<ItemRequest> itemsRequest) {

        return inventoryResultProto.toReservationResult(stub.withDeadlineAfter(3, TimeUnit.SECONDS)
                .reserveItems(ReserveRequest.newBuilder()
                        .setId(requestId)
                        .addAllItems(inventoryProtoMapper.toReserveItemRequestList(itemsRequest))
                        .build()
                ));
    }

    private List<ReservationItemResult> fallback(List<ItemRequest> itemsRequest, Throwable t) {
        log.warn("Fallback response from gRPC, itemsId = {}", itemsRequest, t);

        return Collections.emptyList();

    }


}

