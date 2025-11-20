package com.example.transport_marketplace.exceptions.payment;

public class PaymentHasConflictsException extends RuntimeException {
    public PaymentHasConflictsException() {
        super("У платежа появился конфликт, обратитесь в поддержку!");
    }
}
