package com.example.transport_marketplace.config;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.*;
import com.example.transport_marketplace.repo.AccountRepository;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.RouteRepository;
import com.example.transport_marketplace.repo.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final RouteRepository routeRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final AccountRepository accountRepository;
    private void createUserIfNotExists(String username, String password, Role role, @Nullable String email, List<Device> devices) {
        if (!userRepository.existsByUsername(username)) {
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .email(email)
                    .devices(devices)
                    .build();
            userRepository.save(user);
        }
    }
    @EventListener(ApplicationReadyEvent.class)
    public void initAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("secure_admin"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initFirstAndAccountUser() {
        if (!userRepository.existsByUsername("user")){
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user_password"))
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);

            Account account = Account.builder()
                    .user(user)
                    .balance(1000)
                    .build();
            accountRepository.save(account);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void initTestUsers() {
        createUserIfNotExists("manager", "managerPass123!", Role.ROLE_ADMIN, null, null);
        createUserIfNotExists("test_user", "testPassword456", Role.ROLE_USER, "email@example.com", null);
        createUserIfNotExists("demo_admin", "DemoAdminSecure789", Role.ROLE_ADMIN, null, null);
        createUserIfNotExists("integration_test", "IntegrationTestPass!", Role.ROLE_USER, null, null);
        createUserIfNotExists("traveler1", "Travel123!", Role.ROLE_USER, null, null);
        createUserIfNotExists("traveler2", "SecurePass#2024", Role.ROLE_USER, null, null);
        createUserIfNotExists("business_user", "BusinessTravel99", Role.ROLE_USER, null, null);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    public void initRoutes() {
        createRouteIfNotExists(
                "Челябинск",
                "Омск",
                "2026-12-27",
                "2026-12-27 08:00:00",
                "2026-12-27 12:30:00",
                "Авиа",
                55,
                3100
        );
        createRouteIfNotExists(
                "Москва",
                "Санкт-Петербург",
                "2026-07-15",
                "2026-07-15 10:30:00",
                "2026-07-15 14:45:00",
                "Поезд",
                120,
                2500
        );
        createRouteIfNotExists(
                "Новосибирск",
                "Красноярск",
                "2026-08-01",
                "2026-08-01 07:45:00",
                "2026-08-01 10:15:00",
                "Авиа",
                40,
                4200
        );
    }

    private void createRouteIfNotExists(String from, String to, String date,
                                        String time, String arrivalTime,
                                        String transport, int seats, double price) {
        if (!routeRepository.existsByRouteFromAndRouteToAndDate(from, to, date)) {
            Route route = Route.builder()
                    .routeFrom(from)
                    .routeTo(to)
                    .date(date)
                    .time(time)
                    .arrivalTime(arrivalTime)
                    .transport(transport)
                    .availableSeats(seats)
                    .price(price)
                    .build();

            routeRepository.save(route);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(3)
    public void initBookings() {
        createBookingIfNotExists(
                "demo_admin",
                "Москва",
                "Санкт-Петербург",
                "2026-07-15"
        );
        createBookingIfNotExists(
                "manager",
                "Челябинск",
                "Омск",
                "2026-12-27"
        );
        createBookingIfNotExists(
                "test_user",
                "Новосибирск",
                "Красноярск",
                "2026-08-01"
        );
    }

    private void createBookingIfNotExists(String username, String from,
                                          String to, String routeDate) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User " + username + " not found"));

            Route route = routeRepository.findByRouteFromAndRouteToAndDate(from, to, routeDate);

            if (!bookingRepository.existsByUserAndRoute(user, route)) {
                Booking booking = Booking.builder()
                        .user(user)
                        .route(route)
                        .status(BookingStatus.PENDING)
                        .build();

                bookingRepository.save(booking);
            }
        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
        }
    }
}
