package com.example.transport_marketplace.service;

import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.events.ConfirmationCodeEvent;
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

import java.time.LocalDateTime;

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

    public Payment createPayment(String accessToken, Integer bookingId, PaymentMethod method) {
        User user = userRepository.findByUsername(jwtService.getUsernameFromToken(accessToken))
                .orElseThrow(() -> new RuntimeException("User was not found by this token"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking was not found by this id"));

        String storedCode = CodeGenerator.generateCode();
        LocalDateTime codeExpiration = CodeGenerator.generateExpiryTime();

        Payment payment = Payment.builder()
                .amount(booking.getRoute().getPrice())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(method)
                .description("Оплата бронирования #" + booking.getId()
                        + " ("  + booking.getRoute().getRouteFrom() + " - " + booking.getRoute().getRouteTo() + ")")
                .confirmationCode(storedCode)
                .codeExpiresAt(codeExpiration)
                .booking(booking)
                .user(user)
                .build();

        sendConfirmationCode(user, storedCode, payment);

        return paymentRepository.save(payment);
    }

    public void sendConfirmationCode(User user, String code, Payment payment){
        try{
            ConfirmationCodeEvent event = ConfirmationCodeEvent.builder()
                    .userEmail(user.getEmail())
                    .userName(user.getUsername())
                    .confirmationCode(code)
                    .paymentId(payment.getId())
                    .amount(payment.getAmount())
                    .expiresAt(payment.getCodeExpiresAt())
                    .build();

            rabbitTemplate.convertAndSend(
                    "notification.exchange",
                    "confirmation.code",
                    event
            );

            log.info("Confirmation code {} sent to user {}", code, user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation code to user {}", user.getEmail(), e);
        }
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
