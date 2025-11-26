package com.example.transport_marketplace.exceptions.booking;

public class BookingPaidException extends RuntimeException {
    public BookingPaidException() {
        super("Этот букинг уже был оплачен!");
    }
}
