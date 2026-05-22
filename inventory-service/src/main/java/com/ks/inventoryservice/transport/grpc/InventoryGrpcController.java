package com.ks.inventoryservice.transport.grpc;

import com.ks.inventoryservice.item.application.ReservationApplicationService;
import com.ks.items.v1.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;


@Slf4j
@GrpcService
@RequiredArgsConstructor
public class InventoryGrpcController extends ItemsInventoryServiceGrpc.ItemsInventoryServiceImplBase {

    private final ReservationApplicationService reservationApplicationService;

    @Override
    public void reserveItems(ReserveRequest request, StreamObserver<ReserveResponse> responseObserver) {

        try {
            responseObserver.onNext(reservationApplicationService.reservationRequestHandler(request));
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error gRPC server: ", e);
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Inventory service unavailable").asRuntimeException()
            );
        }
    }
}
