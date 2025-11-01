package com.minvoice.demo.application.services.exceptions;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
    public ItemNotFoundException() {
        super("Item not found");
    }
}
