package com.ks.inventoryservice.exception;


public class ItemNotFoundException extends BusinessException {
    public ItemNotFoundException(Long id) {
        super("Item not found: " + id);
    }
}
