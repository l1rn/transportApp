package com.example.transport_marketplace.exceptions.payment;

public class PaymentHasProcessedException extends RuntimeException {
    private final String existingPaymentId;
    public PaymentHasProcessedException(String existingPaymentId) {
        super("The payment has been created and proceeded");
        this.existingPaymentId = existingPaymentId;
    }

    public String getExistingPaymentId() {
        return existingPaymentId;
    }
}
