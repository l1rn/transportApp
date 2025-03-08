package com.example.transport_marketplace.exceptions.booking;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
