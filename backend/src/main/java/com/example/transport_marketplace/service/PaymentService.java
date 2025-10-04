package com.example.transport_marketplace.service;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.events.PaymentSuccessEvent;
import com.example.transport_marketplace.model.*;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final RabbitTemplate rabbitTemplate;
    private final RouteRepository routeRepository;
    private final BookingRepository bookingRepository;

    private void sendPaymentSuccessEvent(Payment payment){
        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .userEmail(payment.getUser().getEmail())
                .userName(payment.getUser().getUsername())
                .paymentMethod(payment.getPaymentMethod())
                .build();
    }
}
