package com.example.transport_marketplace.config;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.RouteRepository;
import com.example.transport_marketplace.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RouteRepository routeRepository;
    private final BookingRepository bookingRepository;
    private void createUserIfNotExists(String username, String password, Role role) {
        if (!userRepository.existsByUsername(username)) {
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(role)
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
    public void initFirstUser() {
        if (!userRepository.existsByUsername("user")){
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user_password"))
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initTestUsers() {
        createUserIfNotExists("manager", "managerPass123!", Role.ROLE_ADMIN);
        createUserIfNotExists("test_user", "testPassword456", Role.ROLE_USER);
        createUserIfNotExists("demo_admin", "DemoAdminSecure789", Role.ROLE_ADMIN);
        createUserIfNotExists("integration_test", "IntegrationTestPass!", Role.ROLE_USER);
        createUserIfNotExists("traveler1", "Travel123!", Role.ROLE_USER);
        createUserIfNotExists("traveler2", "SecurePass#2024", Role.ROLE_USER);
        createUserIfNotExists("business_user", "BusinessTravel99", Role.ROLE_USER);
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
        createRouteIfNotExists("Новосибирск", "Красноярск", "2026-08-01", "2026-08-01 07:45:00", "2026-08-01 10:15:00", "Авиа", 40, 4200);
        createRouteIfNotExists("Казань", "Уфа", "2026-09-10", "2026-09-10 12:00:00", "2026-09-10 18:30:00", "Автобус", 25, 1800);
        createRouteIfNotExists("Сочи", "Ростов-на-Дону", "2026-10-05", "2026-10-05 09:15:00", "2026-10-05 15:45:00", "Поезд", 60, 2100);
        createRouteIfNotExists("Владивосток", "Хабаровск", "2026-11-20", "2026-11-20 14:30:00", "2026-11-20 16:00:00", "Авиа", 35, 3800);
        createRouteIfNotExists("Самара", "Волгоград", "2026-01-12", "2026-01-12 06:00:00", "2026-01-12 10:30:00", "Автобус", 30, 1500);
        createRouteIfNotExists("Калининград", "Мурманск", "2026-02-14", "2026-02-14 11:45:00", "2026-02-14 14:15:00", "Авиа", 20, 5500);
        createRouteIfNotExists("Пермь", "Екатеринбург", "2026-03-08", "2026-03-08 08:30:00", "2026-03-08 12:00:00", "Поезд", 50, 1900);
        createRouteIfNotExists("Тюмень", "Омск", "2026-04-25", "2026-04-25 13:15:00", "2026-04-25 17:45:00", "Автобус", 40, 2200);
        createRouteIfNotExists("Иркутск", "Красноярск", "2026-05-30", "2026-05-30 16:00:00", "2026-05-30 18:30:00", "Авиа", 45, 4100);
        createRouteIfNotExists("Ярославль", "Нижний Новгород", "2026-06-10", "2026-06-10 10:00:00", "2026-06-10 14:20:00", "Поезд", 55, 2300);
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
                "2026-07-15",
                "BOOKED"
        );
        createBookingIfNotExists(
                "manager",
                "Челябинск",
                "Омск",
                "2026-12-27",
                "BOOKED"
        );

        createBookingIfNotExists(
                "test_user",
                "Москва",
                "Санкт-Петербург",
                "2026-07-15",
                "CANCELED"
        );
        createBookingIfNotExists("user", "Новосибирск", "Красноярск", "2026-08-01", "BOOKED");
        createBookingIfNotExists("integration_test", "Казань", "Уфа", "2026-09-10", "PENDING");
        createBookingIfNotExists("test_user", "Сочи", "Ростов-на-Дону", "2026-10-05", "CANCELED");
        createBookingIfNotExists("manager", "Владивосток", "Хабаровск", "2026-11-20", "BOOKED");
        createBookingIfNotExists("demo_admin", "Самара", "Волгоград", "2026-01-12", "CONFIRMED");
        createBookingIfNotExists("user", "Калининград", "Мурманск", "2026-02-14", "BOOKED");
        createBookingIfNotExists("integration_test", "Пермь", "Екатеринбург", "2026-03-08", "CANCELED");
        createBookingIfNotExists("test_user", "Тюмень", "Омск", "2026-04-25", "PENDING");
        createBookingIfNotExists("manager", "Иркутск", "Красноярск", "2026-05-30", "BOOKED");
        createBookingIfNotExists("demo_admin", "Ярославль", "Нижний Новгород", "2026-06-10", "CONFIRMED");
    }

    private void createBookingIfNotExists(String username, String from,
                                          String to, String routeDate,
                                          String statusStr) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User " + username + " not found"));

            Route route = routeRepository.findByRouteFromAndRouteToAndDate(from, to, routeDate);

            BookingStatus status = BookingStatus.valueOf(statusStr);

            if (!bookingRepository.existsByUserAndRoute(user, route)) {
                Booking booking = Booking.builder()
                        .user(user)
                        .route(route)
                        .status(status)
                        .build();

                bookingRepository.save(booking);
            }
        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
        }
    }
}
