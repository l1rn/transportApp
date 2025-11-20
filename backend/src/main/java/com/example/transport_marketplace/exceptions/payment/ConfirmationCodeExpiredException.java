package com.example.transport_marketplace.exceptions.payment;

public class ConfirmationCodeExpiredException extends RuntimeException {
    public ConfirmationCodeExpiredException() {
        super("Confirmation code was expired");
    }
}
