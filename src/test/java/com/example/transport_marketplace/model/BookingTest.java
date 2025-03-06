package com.example.transport_marketplace.model;

import com.example.transport_marketplace.enums.BookingStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void testBookingBuilder() {
        Route route = new Route();
        route.setId(1);
        User user = new User(1, "user1", "password", null);
        Booking booking = Booking.builder()
                .id(100)
                .route(route)
                .user(user)
                .status(BookingStatus.BOOKED)
                .build();

        assertEquals(100, booking.getId());
        assertEquals(route, booking.getRoute());
        assertEquals(user, booking.getUser());
        assertEquals(BookingStatus.BOOKED, booking.getStatus());
    }

    @Test
    void testBookingSettersAndGetters() {
        Booking booking = new Booking();
        Route route = new Route();
        route.setId(2);
        User user = new User(2, "user2", "password", null);
        booking.setId(200);
        booking.setRoute(route);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CANCELED);

        assertEquals(200, booking.getId());
        assertEquals(route, booking.getRoute());
        assertEquals(user, booking.getUser());
        assertEquals(BookingStatus.CANCELED, booking.getStatus());
    }
}
