package com.example.transport_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {
    private String routeFrom;
    private String routeTo;
    private String transport;
    private LocalDateTime destinationTime;
    private LocalDateTime arrivalTime;
    private Integer availableSeats;
    private Double price;
}
