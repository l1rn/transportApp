package com.example.transport_marketplace.controllers;


import com.example.transport_marketplace.enums.Role;
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


import java.time.Instant;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> getMyBooking(@RequestHeader("Authorization") String authHeader){
        var token = authHeader.substring(7);
        Integer userId = jwtService.extractUserId(token);
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован.");
        }
        List<Booking> bookings = bookingService.getBookingByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @Operation(summary = "Все брони пользователей только для админа")
    @GetMapping
    public ResponseEntity<?> getAllBookingsForUserTest(@RequestHeader(value = "Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Требуется токен");
        }
        String token = authHeader.substring(7);
        String role = jwtService.extractRole(token);

        // Используйте Enum для проверки роли
        if (Role.ROLE_ADMIN.name().equals(role)) {
            return ResponseEntity.ok(bookingService.getAllBooking());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недостаточно прав");
        }
    }
    //
    @Operation(summary = "Забронировать только для авторизованных")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        int userId = user.getId();

        try {
            Booking newBooking = bookingService.createBooking(request.getRouteId(), userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        }
            catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Отмена брони")
    @PatchMapping("/my/{id}/cancel")
    public ResponseEntity<?> cancelBooking(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int id
    ) {
        String token = authHeader.substring(BEARER_PREFIX.length());
        Integer userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован");
        }

        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Бронирование не найдено");
        }

        Booking booking = bookingOpt.get();
        boolean isAdmin = Role.ROLE_ADMIN.name().equals(role);
        boolean isOwner = booking.getUser().getId() == userId;

        if (!isOwner && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет прав");
        }

        if (bookingService.cancelBooking(id)) {
            return ResponseEntity.ok("Бронирование отменено");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка отмены");
        }
    }

    @Operation(summary = "Поиск брони по id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingId(@RequestHeader("Authorization") String authHeader, @PathVariable int id){
        var token = authHeader.substring(BEARER_PREFIX.length());
        Integer userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован.");
        }
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if(bookingOpt.isPresent()){
            Booking booking = bookingOpt.get();
            if(booking.getUser().getId() == userId || "ADMIN".equals(role)){
                return ResponseEntity.ok(booking);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет доступа к этой брони");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такой брони не существует");
    }

}
