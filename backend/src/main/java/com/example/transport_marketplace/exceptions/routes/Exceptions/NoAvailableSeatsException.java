package com.example.transport_marketplace.exceptions.routes.Exceptions;

public class NoAvailableSeatsException extends RuntimeException {
    public NoAvailableSeatsException(String message) {
        super(message);
    }
}
