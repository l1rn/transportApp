package com.example.transport_marketplace.exceptions.payment;

public class PaymentAlreadyConfirmedException extends RuntimeException {
    public PaymentAlreadyConfirmedException(String message) {
        super(message);
    }
}
