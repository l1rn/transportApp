package com.example.transport_marketplace.entity.bookings;

import com.example.transport_marketplace.entity.routes.Route;
import com.example.transport_marketplace.entity.routes.RouteRepository;
import com.example.transport_marketplace.entity.users.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    private RouteRepository routeRepository;
    // получение всех броней
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Booking>> getAllBookings(){
        return ResponseEntity.ok(bookingService.getAllBooking());
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Booking>> getMyBooking(int userId){
        User current = getCurrentUser();
        List<Booking> bookings = bookingService.getBookingByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request){
        User currentUser = getCurrentUser();
        try {
            Booking newBooking = bookingService.createBooking(request.getRouteId(), currentUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cancelBooking(@PathVariable int id){
        User currentUser = getCurrentUser();
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if(bookingOpt.isPresent()){
            Booking booking = bookingOpt.get();
            if(booking.getUser().getId() == currentUser.getId() || currentUser.getRole().equals("ADMIN"))
                if(bookingService.cancelBooking(id)){
                    return ResponseEntity.ok("Бронирование отменено");
                }
        }
        else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вы не можете отменить это бронирование");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Бронирование не найдено.");
    }
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getBookingId(@PathVariable int id){
        User currentUser = getCurrentUser();
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if(bookingOpt.isPresent()){
            Booking booking = bookingOpt.get();
            if(booking.getUser().getId() == currentUser.getId() || currentUser.getRole().equals("ADMIN")){
                return ResponseEntity.ok(booking);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет доступа к этой брони");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Такой брони не существует");
    }

    private User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
