package com.example.transport_marketplace.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void testRouteCreation() {
        Route route = Route.builder()
                .id(1)
                .routeFrom("Moscow")
                .routeTo("Saint Petersburg")
                .transport("Train")
                .destinationTime(LocalDateTime.of(2026, 10, 24, 20, 20))
                .arrivalTime(LocalDateTime.of(2026, 10, 24, 22, 20))
                .availableSeats(50)
                .price(1000.00)
                .build();

        assertEquals(1, route.getId());
        assertEquals("Moscow", route.getRouteFrom());
        assertEquals("Saint Petersburg", route.getRouteTo());
        assertEquals("Train", route.getTransport());
        assertEquals(LocalDateTime.of(2026, 10, 24, 20, 20), route.getDestinationTime());
        assertEquals(LocalDateTime.of(2026, 10, 24, 22, 20), route.getArrivalTime());
        assertEquals(50, route.getAvailableSeats());
        assertEquals(1000.00, route.getPrice());
    }

    @Test
    void testSetters() {
        Route route = new Route();
        route.setRouteFrom("Moscow");
        route.setRouteTo("Kazan");
        route.setTransport("Bus");
        route.setDestinationTime(LocalDateTime.of(2026, 10, 24, 20, 20));
        route.setArrivalTime(LocalDateTime.of(2026, 10, 24, 22, 20));
        route.setAvailableSeats(40);
        route.setPrice(750.75);

        assertEquals("Moscow", route.getRouteFrom());
        assertEquals("Kazan", route.getRouteTo());

        assertEquals("Bus", route.getTransport());
        assertEquals(LocalDateTime.of(2026, 10, 24, 20, 20), route.getDestinationTime());
        assertEquals(LocalDateTime.of(2026, 10, 24, 22, 20), route.getArrivalTime());
        assertEquals(40, route.getAvailableSeats());
        assertEquals(750.75, route.getPrice());
    }
}
