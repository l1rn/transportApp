package com.example.transport_marketplace.service;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.events.PaymentSuccessEvent;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.*;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.PaymentRepository;
import com.example.transport_marketplace.repo.RouteRepository;
import com.example.transport_marketplace.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final RouteRepository routeRepository;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookingRepository bookingRepository;

    public Payment createPayment(String accessToken, Integer bookingId, PaymentMethod method){
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("User was not found by this token"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking was not found by this id"));

        Payment payment = Payment.builder()
                .amount(booking.getRoute().getPrice())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(method)
                .description("Оплата бронирования #" + booking.getId()
                        + " ("  + booking.getRoute().getRouteFrom() + " - " + booking.getRoute().getRouteTo() + ")")
                .booking(booking)
                .user(user)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return savedPayment;
    }

    private void sendPaymentSuccessEvent(Payment payment){
        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .userEmail(payment.getUser().getEmail())
                .userName(payment.getUser().getUsername())
                .paymentMethod(payment.getPaymentMethod())
                .build();

        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.success",
                event
        );
    }
}
