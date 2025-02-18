package com.example.transport_marketplace.Routes;

import com.example.transport_marketplace.Routes.Exceptions.BadRequestException;
import com.example.transport_marketplace.Routes.Exceptions.RouteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RouteNotFoundAdvice {
    @ExceptionHandler(RouteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String routeNotFoundHandler(RouteNotFoundException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String routeBadRequestHandler(BadRequestException ex){
        return ex.getMessage();
    }
}
