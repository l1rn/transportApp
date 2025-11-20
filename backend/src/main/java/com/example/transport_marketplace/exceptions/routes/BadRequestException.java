package com.example.transport_marketplace.exceptions.routes;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){
        super("Bad request");
    }
}
