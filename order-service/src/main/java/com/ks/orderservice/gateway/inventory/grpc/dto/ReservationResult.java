package com.ks.orderservice.gateway.inventory.grpc.dto;

import java.util.List;

public record ReservationResult(
        String errorMessage,
        Status status,
        List<ReservationItemResult> resultList
) {

    public enum Status {
        SUCCESS,
        FAILURE,
        PARTIAL
    }
}
