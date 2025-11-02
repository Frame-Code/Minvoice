package com.minvoice.demo.application.services.exceptions;

public class InvoiceNotFoundException extends RuntimeException {
    public InvoiceNotFoundException(String message) {
        super(message);
    }
    public InvoiceNotFoundException() {
        super("Can't find invoice");
    }
}
