package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.BookingController;
import com.example.transport_marketplace.dto.booking.BookingRequest;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.JwtAuthenticationFilter;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.jwt.TokenBlacklist;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.BookingService;
import com.example.transport_marketplace.service.RouteService;
import com.example.transport_marketplace.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    List<Route> mockRoutes;

    @Mock
    private RouteService routeService;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklist tokenBlacklist;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private BookingService bookingService;

    private User user;

    @InjectMocks
    private BookingController bookingController;
    @BeforeEach
    void setupBooking() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .addFilter(new JwtAuthenticationFilter(jwtService, userDetailsService, tokenBlacklist))
                .defaultRequest(get("/").with(csrf()).characterEncoding(StandardCharsets.UTF_8))
                .build();

        mockRoutes = List.of(
                new Route(1,"Челябинск", "Омск", "2026-12-27", "Авиа",
                        "2026-12-27 08:00:00", "2026-12-27 12:30:00", 55, 3100),
                new Route(2, "Москва", "Санкт-Петербург", "2026-07-15", "Поезд",
                        "2026-07-15 10:30:00", "2026-07-15 14:45:00", 120, 2500),
                new Route(3, "Новосибирск", "Красноярск", "2026-08-01", "Авиа",
                        "2026-08-01 07:45:00", "2026-08-01 10:15:00", 40, 4200),
                new Route(4,"Челябинск", "Омск", "2026-12-24", "Поезд",
                        "2026-12-27 08:00:00", "2026-12-27 12:30:00", 100, 3200)
        );

        user = User.builder()
                .username("test")
                .password("123")
                .role(Role.ROLE_USER)
                .build();

        when(userDetailsService.loadUserByUsername("test")).thenReturn(user);
    }

//    @Test
//    @WithUserDetails("test")
//    void createBooking() throws Exception{
//        int routeId = mockRoutes.getFirst().getId();
//        BookingRequest request = new BookingRequest();
//        request.setRouteId(routeId);
//
//        when(routeService.getRouteById(routeId)).thenReturn(Optional.of(mockRoutes.getFirst()));
//        when(bookingService.createBooking(routeId, user.getId())).thenReturn(new Booking());
//
//        mockMvc.perform(post("/api/profile/bookings")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk());
//    }
}
