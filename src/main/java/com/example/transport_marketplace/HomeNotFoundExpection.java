package com.example.transport_marketplace;

public class HomeNotFoundExpection extends RuntimeException{
    HomeNotFoundExpection(){
        super("Страница не найдена");
    }
}
