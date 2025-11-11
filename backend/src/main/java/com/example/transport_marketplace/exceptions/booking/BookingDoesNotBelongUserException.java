package com.example.transport_marketplace.exceptions.booking;

public class BookingDoesNotBelongUserException extends RuntimeException {
    public BookingDoesNotBelongUserException(String message) {
        super(message);
    }
}
