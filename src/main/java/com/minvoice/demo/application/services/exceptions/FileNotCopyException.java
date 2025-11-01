package com.minvoice.demo.application.services.exceptions;

public class FileNotCopyException extends RuntimeException {
    public FileNotCopyException(String message) {
        super(message);
    }
    public FileNotCopyException() {
        super("File was not copied");
    }
}
