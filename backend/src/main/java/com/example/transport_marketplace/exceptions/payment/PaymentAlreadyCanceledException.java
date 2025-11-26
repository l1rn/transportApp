package com.example.transport_marketplace.exceptions.payment;

public class PaymentAlreadyCanceledException extends RuntimeException {
    public PaymentAlreadyCanceledException(String message) {
        super(message);
    }
}
