package com.ks.orderservice.exception;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String description) {
        super(description);
    }
}
