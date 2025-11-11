package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.BookingController;
import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.JwtAuthenticationFilter;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.jwt.TokenBlacklist;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.BookingService;
import com.example.transport_marketplace.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    List<Route> mockRoutes;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklist tokenBlacklist;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private User user;

    @BeforeEach
    void setupBooking() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .addFilter(new JwtAuthenticationFilter(jwtService, userDetailsService, tokenBlacklist))
                .defaultRequest(get("/").with(csrf()))
                .build();
        mockRoutes = List.of(
                Route.builder()
                        .id(1)
                        .routeFrom("Челябинск")
                        .routeTo("Омск")
                        .transport("Авиа")
                        .destinationTime(LocalDateTime.of(2026, 12, 27, 8, 0))
                        .arrivalTime(LocalDateTime.of(2026, 12, 27, 12, 30))
                        .availableSeats(55)
                        .price(3100)
                        .build(),

                Route.builder()
                        .id(2)
                        .routeFrom("Москва")
                        .routeTo("Санкт-Петербург")
                        .transport("Поезд")
                        .destinationTime(LocalDateTime.of(2026, 7, 15, 10, 30))
                        .arrivalTime(LocalDateTime.of(2026, 7, 15, 14, 45))
                        .availableSeats(120)
                        .price(2500)
                        .build(),

                Route.builder()
                        .id(3)
                        .routeFrom("Новосибирск")
                        .routeTo("Красноярск")
                        .transport("Авиа")
                        .destinationTime(LocalDateTime.of(2026, 8, 1, 7, 45))
                        .arrivalTime(LocalDateTime.of(2026, 8, 1, 10, 15))
                        .availableSeats(40)
                        .price(4200)
                        .build(),

                Route.builder()
                        .id(5)
                        .routeFrom("Челябинск")
                        .routeTo("Омск")
                        .transport("Поезд")
                        .destinationTime(LocalDateTime.of(2026, 12, 27, 8, 0))
                        .arrivalTime(LocalDateTime.of(2026, 12, 27, 12, 30))
                        .availableSeats(100)
                        .price(3200)
                        .build()
        );
       user = User.builder()
                .id(100)
                .username("123")
                .password("123")
                .role(Role.ROLE_USER)
                .build();

    }
//    @Test
//    @WithMockUser(roles = "ROLE_ADMIN")
//    void getAllBookings() throws Exception {
//        Booking mockBooking = new Booking();
//        mockBooking.setStatus(BookingStatus.PENDING);
//        mockBooking.setUser(user);
//        mockBooking.setRoute(mockRoutes.getFirst());
//
//        List<Booking> bookings = Collections.singletonList(mockBooking);
//
//        when(bookingService.getAllBookings()).thenReturn(bookings);
//        mockMvc.perform(get("/api/profile/bookings/all"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].user.username").value(user.getUsername()));
//
//    }
}