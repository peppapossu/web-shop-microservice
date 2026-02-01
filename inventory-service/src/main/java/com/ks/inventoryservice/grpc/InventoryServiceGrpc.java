package com.ks.inventoryservice.grpc;

import com.ks.common.proto.ProductRequest;
import com.ks.common.proto.ProductResponse;
import com.ks.common.proto.ProductServiceGrpc;
import com.ks.inventoryservice.entity.Item;
import com.ks.inventoryservice.mapper.ProtoMapper;
import com.ks.inventoryservice.service.ItemService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class InventoryServiceGrpc extends ProductServiceGrpc.ProductServiceImplBase {

    private final ItemService itemService;
    private final ProtoMapper protoMapper;

    @Override
    public void checkAvailability(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            Item item = itemService.getItemById(request.getProductId());

            ProductResponse response = protoMapper.toProto(item);

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Error gRPC server, Item availability request", e);
            responseObserver.onError(e);
        }
    }
}
