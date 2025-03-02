package com.example.transport_marketplace.enter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookingRequest {
    @Schema(description = "route id")
    @NotBlank
    private int routeId;
    @Schema(description = "user id")
    @NotBlank
    private int userId;
}
