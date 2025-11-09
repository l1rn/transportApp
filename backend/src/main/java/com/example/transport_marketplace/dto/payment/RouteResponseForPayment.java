package com.example.transport_marketplace.dto.payment;

import com.example.transport_marketplace.model.Route;

public record RouteResponseForPayment (
        int id,
        String name,
        String description
) {
    public static RouteResponseForPayment from(Route route) {
        return new RouteResponseForPayment(
                route.getId(),
                route.getRouteFrom() + " - " + route.getRouteTo(),
                route.getDate()
        );
    }
}
