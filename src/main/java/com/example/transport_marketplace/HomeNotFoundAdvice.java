package com.example.transport_marketplace;

import com.example.transport_marketplace.Routes.Exceptions.BadRequestException;
import com.example.transport_marketplace.Routes.Exceptions.RouteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HomeNotFoundAdvice {
    @ExceptionHandler(HomeNotFoundExpection.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String homeNotFoundAdvice(RouteNotFoundException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String homeBadRequest(BadRequestException ex){
        return ex.getMessage();
    }
}
