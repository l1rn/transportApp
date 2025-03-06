package com.example.transport_marketplace.controllers;


import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.enter.BookingRequest;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.BookingService;
import com.example.transport_marketplace.service.TokenService;
import com.example.transport_marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/bookings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookingController {
    public static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private BookingService bookingService;
    // получение всех броней
    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Отображение только тех броней, что выбрал пользователей")
    @GetMapping("/my")

    public ResponseEntity<?> getMyBooking(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        try{
            List<Booking> bookings = bookingService.getBookingByUser(username);
            return ResponseEntity.ok(bookings);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Все брони пользователей только для админа")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllBookingsForAdmin() {
        return ResponseEntity.ok(bookingService.getAllBooking());
    }

    @Operation(summary = "Забронировать только для авторизованных")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request,
                                           @AuthenticationPrincipal User user){
        try {
            Booking newBooking = bookingService.createBooking(request.getRouteId(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        }
            catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Отмена брони")
    @PatchMapping("/my/{id}/cancel")
    @PreAuthorize("@bookingSecurity.isBookingOwner(#id, principal) || hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> cancelBooking(@PathVariable int id) {
        if(bookingService.cancelBooking(id)){
            return ResponseEntity.ok("Бронирование отменено");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка отмены");
    }

    @Operation(summary = "Поиск брони по id")
    @GetMapping("/{id}")
    @PreAuthorize("@bookingSecurity.canAccessBooking(#id, principal)")
    public ResponseEntity<?> getBookingId(@PathVariable int id) {
        return bookingService.getBookingById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
