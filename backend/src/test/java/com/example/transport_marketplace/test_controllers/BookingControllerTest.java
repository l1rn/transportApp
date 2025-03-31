package com.example.transport_marketplace.test_controllers;


import com.example.transport_marketplace.controllers.BookingController;
import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.booking.BookingRequest;
import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.TokenBlacklist;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.AuthenticationService;
import com.example.transport_marketplace.service.BookingService;
import com.example.transport_marketplace.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import javax.naming.AuthenticationException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;
    @TestConfiguration
    static class Config {
        @Bean
        private BookingService bookingService() { return mock(BookingService.class); }
        @Bean
        private TokenBlacklist tokenBlacklist() { return mock(TokenBlacklist.class); }
        @Bean
        private ObjectMapper objectMapper() { return new ObjectMapper(); }
        @Bean
        private AuthenticationService authenticationService() { return mock(AuthenticationService.class); }
        @Bean
        private UserService userService() {return mock(UserService.class);}
    }

    private User user;
    @BeforeEach
    void initUsers(){
        Device device = Device.builder()
                .id(1)
                .deviceFingerprint("finger")
                .userAgent("win")
                .build();

        user = User.builder()
                .id(1)
                .username("test")
                .password("123")
                .role(Role.ROLE_USER)
                .devices(List.of(device))
                .build();
        device.setUser(user);

        when(userService.getByUsername("test")).thenReturn(user);
    }

//    @Test
//    @WithMockUser(username = "test")
//    void testCreateBooking() throws Exception{
//        Route route = Route.builder()
//                .id(1)
//                .routeFrom("Москва")
//                .routeTo("Санкт-Петербург")
//                .date("2026-07-15")
//                .time("2026-07-15 10:30:00")
//                .arrivalTime("2026-07-15 14:45:00")
//                .availableSeats(120)
//                .price(2500.40)
//                .build();
//
//        BookingRequest bookingRequest = new BookingRequest();
//        bookingRequest.setRouteId(1);
//
//        Booking mockBooking = Booking.builder()
//                .id(1)
//                .user(user)
//                .route(route)
//                .status(BookingStatus.BOOKED)
//                .build();
//        when(bookingService.createBooking(1, 1)).thenReturn(mockBooking);
//
//        mockMvc.perform(post("/api/profile/bookings")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(bookingRequest)))
//    }

}
