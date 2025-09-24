package com.example.transport_marketplace.service;

import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.Account;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.repo.AccountRepository;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final RouteRepository routeRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AccountRepository accountRepository;

    public List<Booking> getBookingByUserId(int userId){
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingByUser(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return bookingRepository.findByUser(user);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Cacheable(value = "booking", key = "#id")
    public Optional<Booking> getBookingById(int id){
        return bookingRepository.findById(id);
    }

    @CachePut(value = "booking", key = "#result.id")
    @Transactional
    public Booking createBooking(int routeId, int userId){

        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Маршрут не найден"));
        if(route.getAvailableSeats() <= 0){
            throw new RuntimeException("Нет свободных мест");
        }

        User user = userRepository.findById(userId).orElseThrow(() ->new RuntimeException("Такого пользователя нет"));
        route.setAvailableSeats(route.getAvailableSeats());
        routeRepository.save(route);

        Booking booking = Booking.builder()
                .route(route)
                .user(user)
                .status(BookingStatus.PENDING)
                .build();

        return bookingRepository.save(booking);
    }

    public boolean confirmBooking(String accessToken, int bookingId){
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("Не удалось найти пользователя"));

        Account account = accountRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Не удалось найти счет"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Не удалось найти бронь"));

        if(booking.getStatus() == BookingStatus.CANCELED){
            return false;
        }

        if(account.getBalance() - booking.getRoute().getPrice() < 0){
            throw new RuntimeException("Недостаточно средств на балансе");
        }

        Route route = booking.getRoute();

        route.setAvailableSeats(route.getAvailableSeats() - 1);
        booking.setStatus(BookingStatus.PAID);
        account.setBalance(account.getBalance() - booking.getRoute().getPrice());

        routeRepository.save(route);
        bookingRepository.save(booking);
        accountRepository.save(account);

        return true;
    }

    @CacheEvict(value = "booking", key = "#bookingId")
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
    @CacheEvict(value = "booking", key = "#id")
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
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteCanceledBookings(){
        bookingRepository.deleteByStatus(BookingStatus.CANCELED);
    }
}
