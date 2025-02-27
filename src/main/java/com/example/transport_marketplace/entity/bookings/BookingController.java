package com.example.transport_marketplace.entity.bookings;


import com.example.transport_marketplace.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    // need to delete after tests
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookingsForUserTest(){
        return ResponseEntity.ok(bookingService.getAllBooking());
    }
    //

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestHeader(value = "Authorization") String authHeader, @RequestBody BookingRequest request){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Требуется токен в заголовке Authorization");
        }
        var token = authHeader.substring(BEARER_PREFIX.length());
        Integer userId = jwtService.extractUserId(token);
        System.out.println("Authorization Header: " + authHeader);

        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован.");
        }

        try {
            Booking newBooking = bookingService.createBooking(request.getRouteId(), userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(newBooking);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@RequestHeader("Authorization") String authHeader, @PathVariable int id){
        var token = authHeader.substring(BEARER_PREFIX.length());
        Integer userId = jwtService.extractUserId(token);
        String role = jwtService.extractRole(token);
        if(userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Пользователь не авторизован.");
        }
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if(bookingOpt.isPresent()){
            Booking booking = bookingOpt.get();
            if(booking.getUser().getId() == userId || "ADMIN".equals(role))
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
