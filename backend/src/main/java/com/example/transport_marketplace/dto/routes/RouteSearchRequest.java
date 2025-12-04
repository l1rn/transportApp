package com.example.transport_marketplace.dto.routes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteSearchRequest {
    private String routeFrom;
    private String routeTo;
    private String transport;
    private String date;
}
