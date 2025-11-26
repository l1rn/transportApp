package com.example.transport_marketplace.exceptions.user;

public class UserHasNoEmailException extends RuntimeException {
    public UserHasNoEmailException(String message) {
        super(message);
    }
}
