package com.example.transport_marketplace.entity.routes.Exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super("Bad request");
    }
}
