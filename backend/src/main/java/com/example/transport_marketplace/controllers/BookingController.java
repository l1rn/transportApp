package com.example.transport_marketplace.controllers;


import com.example.transport_marketplace.dto.booking.BookingRequest;
import com.example.transport_marketplace.dto.booking.CancelBookingRequest;
import com.example.transport_marketplace.exceptions.booking.AccessDeniedException;
import com.example.transport_marketplace.exceptions.booking.BookingNotFoundException;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.BookingService;
import com.example.transport_marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile/bookings")
@RequiredArgsConstructor
public class BookingController {
    public static final String BEARER_PREFIX = "Bearer ";

    private final BookingService bookingService;

    private final UserService userService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(
            summary = "Получение своих бронирований",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Возвращает список бронирований, сделанных текущим аутентифицированным пользователем."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список бронирований пользователя",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Booking.class)))),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "404", description = "Бронирования не найдены для данного пользователя")
    })
    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyBooking(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        try {
            List<Booking> bookings = bookingService.getBookingByUser(username);
            return ResponseEntity.ok(bookings);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Получение всех бронирований (для администраторов)",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Возвращает список всех бронирований в системе. Доступно только пользователям с ролью ROLE_ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех бронирований",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Booking.class)))),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён (не администратор)")
    })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllBookingsForAdmin() {
        return ResponseEntity.ok(bookingService.getAllBooking());
    }

    @Operation(
            summary = "Создание бронирования",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Позволяет аутентифицированному пользователю забронировать маршрут по указанному ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Бронирование успешно создано",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Booking.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или маршрут не найден"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createBooking(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String username = userDetails.getUsername();
        User user = userService.getByUsername(username);
        try {
            Booking newBooking = bookingService.createBooking(request.getRouteId(), user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Отмена бронирования администратором",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Позволяет администратору отменить бронирование по его ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирование успешно отменено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "\"Бронь успешно отменена\""))),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "409", description = "Бронирование уже отменено"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён (не администратор)"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBookingAdmin(
            @PathVariable(name = "id") int id
    ) {
        try {
            boolean response = bookingService.cancelBookingAdmin(id);
            if (response) {
                return ResponseEntity.ok("Бронь успешно отменена");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Бронирование уже отменено");
            }
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Отмена своего бронирования",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Позволяет аутентифицированному пользователю отменить своё бронирование по ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Бронирование успешно отменено",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "\"Бронирование отменено\""))),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён (попытка отменить чужое бронирование)"),
            @ApiResponse(responseCode = "404", description = "Бронирование не найдено"),
            @ApiResponse(responseCode = "409", description = "Бронирование уже отменено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PatchMapping("/my/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelBooking(
            @RequestBody CancelBookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String username = userDetails.getUsername();
            boolean success = bookingService.cancelBooking(request.getBookingId(), username);
            if (success) {
                return ResponseEntity.ok("Бронирование отменено");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Бронирование уже отменено");
            }
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
