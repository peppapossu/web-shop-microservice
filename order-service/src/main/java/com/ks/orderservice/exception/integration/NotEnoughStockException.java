package com.ks.orderservice.exception.integration;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String description) {
        super(description);
    }
}
