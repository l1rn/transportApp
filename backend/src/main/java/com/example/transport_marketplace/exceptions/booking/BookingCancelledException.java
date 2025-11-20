package com.example.transport_marketplace.exceptions.booking;

public class BookingCancelledException extends RuntimeException {
    public BookingCancelledException() {
        super("Этот букинг уже был отменен!");
    }
}
