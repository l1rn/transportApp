package com.example.transport_marketplace.Routes.Exceptions;

public class RouteNotFoundException extends RuntimeException{

    public RouteNotFoundException(int id){
        super("Не могу найти пользователя" + id);
    }

}
