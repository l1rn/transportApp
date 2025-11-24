package com.example.transport_marketplace.fixtures;

import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class TestFixtures {
    public static Device createTestDevice(){
        return Device.builder()
                .id(1)
                .deviceFingerprint("Firefox")
                .userAgent("Windows")
                .build();
    }

    public static User createTestUser(){
        return User.builder()
                .id(1)
                .username("test")
                .password("test")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .devices(List.of(createTestDevice()))
                .build();
    }

    public static Route createTestRoute(){
        return Route.builder()
                .id(1)
                .routeFrom("Moscow")
                .routeTo("Samara")
                .transport("Bus")
                .destinationTime(LocalDateTime.now().minusHours(3))
                .arrivalTime(LocalDateTime.now())
                .availableSeats(70)
                .price(1000.00)
                .build();
    }

    public static Booking createTestBooking() {
        return Booking.builder()
                .id(1)
                .user(createTestUser())
                .route(createTestRoute())
                .status(BookingStatus.PENDING)
                .build();
    }

    public static Payment createTestPayment() {
        return Payment.builder()
                .id(1)
                .amount(createTestBooking().getRoute().getPrice())
                .paymentMethod(PaymentMethod.ELECTRONIC)
                .paymentStatus(PaymentStatus.PENDING)
                .description(createTestBooking().getRoute().getRouteFrom() + " =>" + createTestBooking().getRoute().getRouteTo())
                .confirmationCode(CodeGenerator.generateCode())
                .codeExpiresAt(CodeGenerator.generateExpiryTime())
                .booking(createTestBooking())
                .user(createTestBooking().getUser())
                .build();
    }
}
