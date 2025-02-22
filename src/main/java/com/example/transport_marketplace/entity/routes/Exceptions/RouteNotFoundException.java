package com.example.transport_marketplace.entity.routes.Exceptions;

public class RouteNotFoundException extends RuntimeException{

    public RouteNotFoundException(int id){
        super("Не могу найти пользователя" + id);
    }

}
