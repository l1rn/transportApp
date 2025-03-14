package com.example.transport_marketplace.dto.routes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание маршрута")
public class RouteRequest {
    @Schema(description = "Пункт отправления", example = "Москва")
    @NotBlank(message = "Пункт отправления обязателен")
    private String routeFrom;

    @Schema(description = "Пункт назначения", example = "Санкт-Петербург")
    @NotBlank(message = "Пункт назначения обязателен")
    private String routeTo;

    @Schema(description = "Дата поездки", example = "2023-12-25")
    @NotBlank(message = "Дата должна быть в будущем")
    private String date;

    @Schema(description = "Время отправления", example = "14:30")
    @NotBlank(message = "Время отправления должно быть в будущем")
    private String departureTime;

    @Schema(description = "Время прибытия", example = "18:00")
    @NotBlank(message = "Время прибытия должно быть в будущем")
    private String arrivalTime;

    @Schema(description = "Тип транспорта", example = "Автобус")
    @NotBlank(message = "Тип транспорта обязателен")
    private String transport;

    @Schema(description = "Количество доступных мест", example = "50")
    @PositiveOrZero(message = "Количество мест не может быть отрицательным")
    private int availableSeats;

    @Schema(description = "Цена поездки", example = "1500.0")
    @Positive(message = "Цена должна быть положительной")
    private double price;
}