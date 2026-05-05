package com.ks.orderservice.gateway.inventory.grpc;

import com.ks.orderservice.dto.order.item.ItemRequest;
import com.ks.orderservice.gateway.inventory.InventoryGateway;
import com.ks.orderservice.service.dto.ReservationItemResult;
import com.ks.orderservice.service.dto.ReservationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
@Profile("dev")
public class MockInventoryGateway implements InventoryGateway {

    public ReservationResult reserve(String requestId, List<ItemRequest> itemsRequest) {

        log.error("Checking availability (gRPC) MOCK");

        List<ReservationItemResult> resultList = new ArrayList<>();

        resultList.add(
                new ReservationItemResult(
                        Long.valueOf(requestId),
                        requestId,
                        1,
                        new BigDecimal(100),
                        0,
                        ReservationItemResult.Availability.AVAILABLE,
                        false
                )
        );
        return new ReservationResult("", ReservationResult.Status.SUCCESS, resultList);
    }
}
