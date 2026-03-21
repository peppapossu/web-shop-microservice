package com.ks.orderservice.grpc;

import com.ks.common.proto.Money;
import com.ks.common.proto.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Profile("dev")
public class ProductClientMock implements ProductClient {

    public ProductResponse checkAvailability(Long productId) {

        log.error("Checking availability (gRPC) MOCK");

        return ProductResponse.newBuilder()
                .setProductId(productId)
                .setProductName(productId.toString())
                .setPrice(Money.newBuilder()
                        .setAmount(100L)
                        .build())
                .setQuantity(1)
                .setSale(0)
                .setTotalPrice(Money.newBuilder()
                                .setAmount(100L)
                                .build())
                .build();
    }
}
