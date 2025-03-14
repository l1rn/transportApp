package com.example.transport_marketplace.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void testRouteCreation() {
        Route route = new Route(1, "Moscow", "Saint Petersburg", "2025-03-10", "Train", "10:00", "14:00", 50, 1000.50);

        assertEquals(1, route.getId());
        assertEquals("Moscow", route.getRouteFrom());
        assertEquals("Saint Petersburg", route.getRouteTo());
        assertEquals("2025-03-10", route.getDate());
        assertEquals("Train", route.getTransport());
        assertEquals("10:00", route.getTime());
        assertEquals("14:00", route.getArrivalTime());
        assertEquals(50, route.getAvailableSeats());
        assertEquals(1000.50, route.getPrice());
    }

    @Test
    void testSetters() {
        Route route = new Route();
        route.setRouteFrom("Moscow");
        route.setRouteTo("Kazan");
        route.setDate("2025-03-12");
        route.setTransport("Bus");
        route.setTime("08:00");
        route.setArrivalTime("16:00");
        route.setAvailableSeats(40);
        route.setPrice(750.75);

        assertEquals("Moscow", route.getRouteFrom());
        assertEquals("Kazan", route.getRouteTo());
        assertEquals("2025-03-12", route.getDate());
        assertEquals("Bus", route.getTransport());
        assertEquals("08:00", route.getTime());
        assertEquals("16:00", route.getArrivalTime());
        assertEquals(40, route.getAvailableSeats());
        assertEquals(750.75, route.getPrice());
    }
}
