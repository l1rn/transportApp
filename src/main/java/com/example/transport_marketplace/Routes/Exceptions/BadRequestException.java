package com.example.transport_marketplace.Routes.Exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super("Bad request");
    }
}
