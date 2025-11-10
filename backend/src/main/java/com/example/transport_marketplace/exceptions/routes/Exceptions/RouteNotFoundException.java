package com.example.transport_marketplace.exceptions.routes.Exceptions;

public class RouteNotFoundException extends RuntimeException{
    public RouteNotFoundException(String message){
        super(message);
    }
}
