package com.example.transport_marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class RouteRequest {
    @NotBlank(message = "Пункт отправления обязателен")
    private String routeFrom;

    @NotBlank(message = "Пункт назначения обязателен")
    private String routeTo;

    @NotBlank(message = "Дата должна быть в будущем")
    private String date;

    @NotBlank(message = "Время отправления должно быть в будущем")
    private String departureTime;

    @NotBlank(message = "Время прибытия должно быть в будущем")
    private String arrivalTime;

    @NotBlank(message = "Тип транспорта обязателен")
    private String transport;

    @PositiveOrZero(message = "Количество мест не может быть отрицательным")
    private int availableSeats;

    @Positive(message = "Цена должна быть положительной")
    private double price;
}