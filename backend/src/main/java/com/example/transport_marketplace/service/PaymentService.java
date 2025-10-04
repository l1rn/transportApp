package com.example.transport_marketplace.service;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Account;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final RabbitTemplate rabbitTemplate;
    private final RouteRepository routeRepository;
    private final BookingRepository bookingRepository;

    public void processSuccessfulPayment(Booking booking){
        Route route = booking.getRoute();
        route.setAvailableSeats(route.getAvailableSeats() - 1);
        booking.setStatus(BookingStatus.PAID);

        routeRepository.save(route);
        bookingRepository.save(booking);
        accountRepository.save(account);

        return true;
    }
}
