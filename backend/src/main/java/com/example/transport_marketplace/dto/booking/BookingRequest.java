package com.example.transport_marketplace.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class BookingRequest {
    @Schema(description = "ID маршрута", example = "195", required = true)
    @NotNull(message = "Поле routeId обязательно для заполнения")
    @Positive(message = "ID маршрута должен быть положительным числом")
    private Integer routeId;
}
