//package com.example.transport_marketplace.test_contorllers;
//
//import com.example.transport_marketplace.controllers.BookingController;
//import com.example.transport_marketplace.enums.BookingStatus;
//import com.example.transport_marketplace.model.Booking;
//import com.example.transport_marketplace.model.User;
//import com.example.transport_marketplace.service.BookingService;
//import com.example.transport_marketplace.jwt.JwtService;
//import com.example.transport_marketplace.service.TokenService;
//import com.example.transport_marketplace.service.UserService;
//import com.example.transport_marketplace.enter.BookingRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = BookingController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//@ContextConfiguration(classes = {BookingController.class, BookingControllerTest.Config.class})
//@Import(BookingControllerTest.Config.class)
//public class BookingControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private BookingService bookingService;
//
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private TokenService tokenService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @TestConfiguration
//    static class Config {
//        @Bean
//        public BookingService bookingService() {
//            return Mockito.mock(BookingService.class);
//        }
//        @Bean
//        public JwtService jwtService() {
//            return Mockito.mock(JwtService.class);
//        }
//        @Bean
//        public UserService userService() {
//            return Mockito.mock(UserService.class);
//        }
//        @Bean
//        public TokenService tokenService() {
//            return Mockito.mock(TokenService.class);
//        }
//        @Bean
//        public ObjectMapper objectMapper() {
//            return new ObjectMapper();
//        }
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    void testGetMyBooking() throws Exception {
//        List<Booking> bookings = List.of(new Booking());
//        when(bookingService.getBookingByUser("user1")).thenReturn(bookings);
//
//        mockMvc.perform(get("/profile/bookings/my")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(bookingService, times(1)).getBookingByUser("user1");
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void testGetAllBookingsForAdmin() throws Exception {
//        List<Booking> bookings = List.of(new Booking(), new Booking());
//        when(bookingService.getAllBooking()).thenReturn(bookings);
//
//        mockMvc.perform(get("/profile/bookings")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(bookings.size()));
//
//        verify(bookingService, times(1)).getAllBooking();
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    void testCreateBooking() throws Exception {
//        BookingRequest request = new BookingRequest();
//        request.setRouteId(1);
//        User user = new User(1, "user1", "pass", null);
//        Booking newBooking = Booking.builder()
//                .id(100)
//                .user(user)
//                .status(BookingStatus.BOOKED)
//                .build();
//        when(userService.findByUsername("user1")).thenReturn(Optional.of(user));
//        when(bookingService.createBooking(eq(1), eq(user.getId()))).thenReturn(newBooking);
//
//        mockMvc.perform(post("/profile/bookings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(100));
//
//        verify(bookingService, times(1)).createBooking(eq(1), eq(user.getId()));
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = {"USER"})
//    void testCancelBooking() throws Exception {
//        when(bookingService.cancelBooking(10)).thenReturn(true);
//
//        mockMvc.perform(patch("/profile/bookings/my/10/cancel")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Бронированик отменено"));
//
//        verify(bookingService, times(1)).cancelBooking(10);
//    }
//}
