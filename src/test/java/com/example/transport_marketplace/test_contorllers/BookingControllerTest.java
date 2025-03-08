//package com.example.transport_marketplace.test_contorllers;
//
//import com.example.transport_marketplace.controllers.BookingController;
//import com.example.transport_marketplace.dto.booking.BookingRequest;
//import com.example.transport_marketplace.dto.booking.CancelBookingRequest;
//import com.example.transport_marketplace.enums.BookingStatus;
//import com.example.transport_marketplace.model.Booking;
//import com.example.transport_marketplace.model.User;
//import com.example.transport_marketplace.service.BookingService;
//import com.example.transport_marketplace.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(BookingController.class)
//public class BookingControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookingService bookingService;
//
//    @MockBean
//    private UserService userService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    void testGetMyBooking_Success() throws Exception {
//        Booking booking = Booking.builder().id(1).build();
//        when(bookingService.getBookingByUser("user1")).thenReturn(List.of(booking));
//
//        mockMvc.perform(get("/profile/bookings/my")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = "ADMIN")
//    void testGetAllBookingsForAdmin_Success() throws Exception {
//        when(bookingService.getAllBooking()).thenReturn(List.of(
//                Booking.builder().id(1).build(),
//                Booking.builder().id(2).build()
//        ));
//
//        mockMvc.perform(get("/profile/bookings"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2));
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    void testCreateBooking_Success() throws Exception {
//        BookingRequest request = new BookingRequest();
//        request.setRouteId(1);
//
//        User user = User.builder().id(1).username("user1").build();
//        Booking newBooking = Booking.builder().id(100).status(BookingStatus.BOOKED).build();
//
//        when(userService.findByUsername("user1")).thenReturn(Optional.of(user));
//        when(bookingService.createBooking(eq(1), eq(1))).thenReturn(newBooking);
//
//        mockMvc.perform(post("/profile/bookings")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(100));
//    }
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    void testCancelBooking_Success() throws Exception {
//        CancelBookingRequest request = new CancelBookingRequest();
//        request.setBookingId(10);
//
//        when(bookingService.cancelBooking(10, "user1")).thenReturn(true);
//
//        mockMvc.perform(patch("/profile/bookings/my/cancel")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Бронирование отменено"));
//    }
//}