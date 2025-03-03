package com.example.transport_marketplace.exceptions.routes.Exceptions;

public class RouteNotFoundException extends RuntimeException{

    public RouteNotFoundException(int id){
        super("Не могу найти пользователя" + id);
    }

}
