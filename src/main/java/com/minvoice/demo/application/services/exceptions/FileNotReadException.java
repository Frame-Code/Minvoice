package com.minvoice.demo.application.services.exceptions;

public class FileNotReadException extends RuntimeException{
    public FileNotReadException(String message) {
        super(message);
    }
    public FileNotReadException() {
        super("Error: error reading the file to load information");
    }
}
