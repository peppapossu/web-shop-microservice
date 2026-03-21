package com.ks.orderservice.grpc;

import com.ks.common.proto.Availability;
import com.ks.common.proto.ProductRequest;
import com.ks.common.proto.ProductResponse;
import com.ks.common.proto.ProductServiceGrpc;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Profile("!dev")
public class ProductClientImpl implements ProductClient {

    @GrpcClient("inventory-service")
    private ProductServiceGrpc.ProductServiceBlockingStub stub;

    @CircuitBreaker(name = "inventory", fallbackMethod = "fallback")
    @Retry(name = "inventory")
    public ProductResponse checkAvailability(Long productId) {

        return stub
                .withDeadlineAfter(3, TimeUnit.SECONDS)
                .checkAvailability(
                        ProductRequest.newBuilder()
                                .setProductId(productId)
                                .build()
                );
    }

    private ProductResponse fallback(Long productId, Throwable t) {
        log.warn("Fallback response from gRPC, itemId = {}", productId, t);

        return ProductResponse.newBuilder()
                .setProductId(productId)
                .setAvailability(Availability.UNKNOWN)
                .setDegraded(true)
                .build();
    }
}
