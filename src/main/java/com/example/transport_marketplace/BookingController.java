package com.example.transport_marketplace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private BookingService bookingService;


    // Получение всех бронирований
    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(){
        return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
    }
    // Создание бронирования
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking){
        Booking newBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable int id){
        boolean removed = bookingService.cancelBooking(id);
        return removed ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingId(@PathVariable int id){
        Optional<Booking> booking = bookingService.getAllBookings()
                .stream()
                .filter(b -> b.getId() == id)
                .findFirst();

        return booking
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Бронирование с ID " + id + " не найдено."));
    }
}
