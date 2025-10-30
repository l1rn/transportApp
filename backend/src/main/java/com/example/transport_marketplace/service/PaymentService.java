package com.example.transport_marketplace.service;

import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.dto.payment.ConfirmPaymentRequest;
import com.example.transport_marketplace.dto.payment.PreparationOrderResponse;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookingRepository bookingRepository;

    public PreparationOrderResponse prepareOrder(String username, Integer bookingId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User was not found by this token"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Не удалось найти заказ по данному id#" + bookingId));

        return PreparationOrderResponse.builder()
                .orderFullName(
                        booking.getRoute().getRouteFrom() + " -> " +
                        booking.getRoute().getRouteTo() + "; " +
                        booking.getRoute().getTransport() + "; Дата: " +
                        booking.getRoute().getDate()
                )
                .paymentMethods(Arrays.stream(PaymentMethod.values()).toList())
                .price(booking.getRoute().getPrice())
                .hasEmail(user.getEmail() != null)
                .build();
    }

    public String createPayment(String username, Integer bookingId, PaymentMethod method) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User was not found by this token"));

        if(user.getEmail() == null){
            throw new RuntimeException("Email не был привязан!");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking was not found by this id"));

        String storedCode = CodeGenerator.generateCode();
        LocalDateTime codeExpiration = CodeGenerator.generateExpiryTime();

        UUID uuid = UUID.randomUUID();

        Payment payment = Payment.builder()
                .amount(booking.getRoute().getPrice())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(method)
                .externalId(uuid)
                .description("Оплата бронирования #" + booking.getId()
                        + " ("  + booking.getRoute().getRouteFrom() + " - " + booking.getRoute().getRouteTo() + ")")
                .confirmationCode(storedCode)
                .codeExpiresAt(codeExpiration)
                .booking(booking)
                .user(user)
                .build();

        paymentRepository.save(payment);

        sendConfirmationCode(user, storedCode, payment);
        return uuid.toString();
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
                    "confirmation.exchange",
                    "confirmation.code",
                    event
            );

            log.info("Confirmation code {} sent to user {}", code, user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation code to user {}", user.getEmail(), e);
        }
    }

    public void confirmPayment(String username, ConfirmPaymentRequest request){
        Payment payment = paymentRepository.findByExternalId(UUID.fromString(request.getExternalId()))
                .orElseThrow(() -> new RuntimeException("Не удалось найти платеж по id"));

        if(!Objects.equals(payment.getConfirmationCode(), request.getCode())){
            throw new RuntimeException("Коды не совпадают!");
        }

        if(!Objects.equals(payment.getUser().getUsername(), username)){
            throw new RuntimeException("Вы не можете подтвердить этот платеж");
        }

        if (payment.getPaymentStatus() == PaymentStatus.CANCELLED){
            throw new RuntimeException("Платеж отменен, создайте новый!");
        }

        if (payment.getPaymentStatus() == PaymentStatus.SUCCEEDED){
            throw new RuntimeException("Платеж уже подтвержден!");
        }

        payment.setPaymentStatus(PaymentStatus.SUCCEEDED);
        paymentRepository.save(payment);

        sendPaymentSuccessEvent(payment);
    }

    private void sendPaymentSuccessEvent(Payment payment){
        Route route = payment.getBooking().getRoute();
        String routeInfo = route.getRouteFrom() + " - " +
                route.getRouteTo() + "; " +
                route.getDate() + "; " +
                route.getTransport();

        PaymentSuccessEvent event = PaymentSuccessEvent.builder()
                .paymentId(payment.getId())
                .bookingId(payment.getBooking().getId())
                .amount(payment.getAmount())
                .routeNumber(routeInfo)
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

    private void cancelPayment(String externalId){
        Payment payment = paymentRepository.findByExternalId(UUID.fromString(externalId))
                .orElseThrow(() -> new RuntimeException("Платеж не найден"));

        if(payment.getPaymentStatus() == PaymentStatus.CANCELLED) {
            throw new RuntimeException("Платеж был уже отменен!");
        }

        payment.setPaymentStatus(PaymentStatus.CANCELLED);
    }
}
