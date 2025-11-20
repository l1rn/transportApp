package com.example.transport_marketplace.exceptions.payment;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException() {
        super("Payment was not found");
    }
}
