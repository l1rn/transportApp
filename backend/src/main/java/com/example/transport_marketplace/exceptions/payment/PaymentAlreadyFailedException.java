package com.example.transport_marketplace.exceptions.payment;

public class PaymentAlreadyFailedException extends RuntimeException {
  public PaymentAlreadyFailedException(String message) {
    super(message);
  }
}
