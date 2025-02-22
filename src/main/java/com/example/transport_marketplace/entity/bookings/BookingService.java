package com.example.transport_marketplace.entity.bookings;

import com.example.transport_marketplace.entity.routes.Route;
import com.example.transport_marketplace.entity.routes.RouteRepository;
import com.example.transport_marketplace.entity.users.User;
import com.example.transport_marketplace.entity.users.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    private BookingRepository bookingRepository;
    private RouteRepository routeRepository;
    private UserRepository userRepository;

    public List<Booking> getBookingByUserId(int userId){
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getAllBooking(){
        return bookingRepository.findAll();
    }
    public Optional<Booking> getBookingById(int id){
        return bookingRepository.findById(id);
    }
    @Transactional
    public Booking createBooking(int routeId, int userId){
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Маршрут не найден"));
        if(route.getAvailableSeats() <= 0){
            throw new RuntimeException("Нет свободных мест");
        }

        User user = userRepository.findById(userId).orElseThrow(() ->new RuntimeException("Такого пользователя нет"));

        route.setAvailableSeats(route.getAvailableSeats() - 1);
        routeRepository.save(route);

        Booking booking = Booking.builder()
                .route(route)
                .user(user)
                .status(BookingStatus.BOOKED)
                .build();

        return bookingRepository.save(booking);
    }

    @Transactional
    public boolean cancelBooking(int id){
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if(bookingOpt.isPresent()){
            Booking booking = bookingOpt.get();
            booking.setStatus(BookingStatus.CANCELED);

            Route route = booking.getRoute();
            route.setAvailableSeats(route.getAvailableSeats() + 1);
            routeRepository.save(route);

            bookingRepository.save(booking);
            return true;
        }
        return false;
    }
}
