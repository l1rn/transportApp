package com.example.transport_marketplace.service;

import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Booking> getBookingByUserId(int userId){
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingByUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getAllBooking() {
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
    public boolean cancelBookingAdmin(int bookingId){

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        if (booking.getStatus() == BookingStatus.CANCELED) {
            return false;
        }

        Route route = booking.getRoute();
        if (route != null) {
            route.setAvailableSeats(route.getAvailableSeats() + 1);
            routeRepository.save(route);
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);

        return true;
    }

    @Transactional
    public boolean cancelBooking(Integer id, String username){

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        if (booking.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("Нет прав для отмены бронирования");
        }

        if (booking.getStatus() == BookingStatus.CANCELED) {
            return false;
        }
        Route route = booking.getRoute();
        if (route != null) {
            route.setAvailableSeats(route.getAvailableSeats() + 1);
            routeRepository.save(route);
        }

        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);

        return true;
    }
}
