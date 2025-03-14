package com.example.transport_marketplace.dto.routes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RouteDTO {
    private int id;
    private String routeFrom;
    private String routeTo;
    private String date;
    private String departureTime;
    private String arrivalTime;
    private String transport;
    private int availableSeats;
    private double price;
}
