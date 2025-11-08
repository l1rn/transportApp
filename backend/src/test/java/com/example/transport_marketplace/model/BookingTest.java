package com.example.transport_marketplace.model;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.Role;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    @Test
    void testBookingBuilder() {
        Route route = new Route();
        route.setId(1);
        User user = User.builder()
                .id(1)
                .username("testUser")
                .password("password123")
                .role(Role.ROLE_USER)
                .build();

        Booking booking = Booking.builder()
                .id(100)
                .route(route)
                .user(user)
                .status(BookingStatus.PENDING)
                .build();

        assertEquals(100, booking.getId());
        assertEquals(route, booking.getRoute());
        assertEquals(user, booking.getUser());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
    }

    @Test
    void testBookingSettersAndGetters() {
        Booking booking = new Booking();
        Route route = new Route();
        route.setId(2);
        User user = User.builder()
                .id(1)
                .username("testUser")
                .password("password123")
                .role(Role.ROLE_USER)
                .build();
        booking.setId(200);
        booking.setRoute(route);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CANCELLED);

        assertEquals(200, booking.getId());
        assertEquals(route, booking.getRoute());
        assertEquals(user, booking.getUser());
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }
}
