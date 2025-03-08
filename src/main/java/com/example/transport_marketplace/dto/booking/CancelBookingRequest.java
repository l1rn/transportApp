package com.example.transport_marketplace.dto.booking;

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
}
