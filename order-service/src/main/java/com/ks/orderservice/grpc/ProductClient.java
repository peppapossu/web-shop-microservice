package com.ks.orderservice.grpc;

import com.ks.common.proto.ProductResponse;

public interface ProductClient {
    ProductResponse checkAvailability(Long productId);
}
