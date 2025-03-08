package com.example.transport_marketplace.controllers;


import com.example.transport_marketplace.dto.booking.BookingRequest;
import com.example.transport_marketplace.dto.booking.CancelBookingRequest;
import com.example.transport_marketplace.exceptions.booking.AccessDeniedException;
import com.example.transport_marketplace.exceptions.booking.BookingNotFoundException;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.security.BookingSecurity;
import com.example.transport_marketplace.service.BookingService;
import com.example.transport_marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BookingController {
    public static final String BEARER_PREFIX = "Bearer ";

    private final BookingService bookingService;

    private final UserService userService;

    private final BookingSecurity bookingSecurity;


    @Operation(summary = "Отображение только тех броней, что выбрал пользователей")
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
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
                                           @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        try {
            Booking newBooking = bookingService.    createBooking(request.getRouteId(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        }
            catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Отмена брони")
    @PatchMapping("/my/cancel")
    public ResponseEntity<?> cancelBooking(@RequestBody CancelBookingRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try{
            String username = userDetails.getUsername();
            boolean success = bookingService.cancelBooking(request.getBookingId(), username);
            if(success){
                return ResponseEntity.ok("Бронирование отменено");
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Бронирование уже отменено");
            }
        }catch (BookingNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (AccessDeniedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
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
