package com.minvoice.demo.application.services.exceptions;

public class StatusNotFoundException extends RuntimeException {
    public StatusNotFoundException(String message) {
        super(message);
    }
    public StatusNotFoundException(){
        super("Error: can't not found status code");
    }
}
