package com.example.transport_marketplace.dto.booking;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CancelBookingRequest {
    @Min(value = 1, message = "ID бронирования должно быть больше 0")
    @NotNull(message = "Booking ID is required")
    private Integer bookingId;
    @Schema(description = "Токен авторизации", example = "eyJhbGciOiJIUzI1NiIsInР5cCI6IkpXVCJ9...")
    private String token;
}
