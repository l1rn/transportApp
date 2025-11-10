package com.example.transport_marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {
    private String routeFrom;
    private String routeTo;
    private String date;
    private String transport;
    private String destinationTime;
    private String arrivalTime;
    private Integer availableSeats;
    private Double price;
}
