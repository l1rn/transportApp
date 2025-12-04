package com.example.transport_marketplace.dto.routes;

import java.time.LocalDateTime;

public record RouteResponse(
    Integer id,
    String routeFrom,
    String routeTo,
    String transport,
    LocalDateTime destinationTime,
    LocalDateTime arrivalTime,
    Integer availableSeats,
    Double price
) {}
