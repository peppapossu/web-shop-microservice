package com.ks.inventoryservice.grpc;

import com.ks.inventoryservice.service.ItemService;
import com.ks.items.v1.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class InventoryServiceGrpc extends ItemsInventoryServiceGrpc.ItemsInventoryServiceImplBase {

    private final ItemService itemService;

    @Override
    public void reserveItems(ReserveRequest request, StreamObserver<ReserveResponse> responseObserver) {

        String errorMessage = "";
        List<ReserveItemResponse> responseList = new ArrayList<>();

        try {
            responseList = itemService.checkAndReserve(request);
        } catch (Exception ex) {
            errorMessage = ex.getMessage();
        }

        try {
            responseObserver.onNext(ReserveResponse.newBuilder()
                    .addAllItemResponse(responseList)
                    .setErrorMessage(errorMessage)
                    .build());
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error gRPC server, Item availability request", e);
            responseObserver.onError(e);
        }
    }
}
