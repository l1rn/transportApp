package com.example.transport_marketplace.service;

import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.dto.payment.ConfirmPaymentRequest;
import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.events.PaymentSuccessEvent;
import com.example.transport_marketplace.exceptions.payment.*;
import com.example.transport_marketplace.exceptions.routes.BadRequestException;
import com.example.transport_marketplace.model.Payment;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentConfirmationService {
    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final PaymentFactoryService paymentFactoryService;

    @Transactional
    public void confirmPayment(String username, ConfirmPaymentRequest request){
        Payment payment = paymentRepository.findByExternalId(UUID.fromString(request.getExternalId()))
                .orElseThrow(() -> new RuntimeException("Не удалось найти платеж по id"));

        payment.setPaymentStatus(PaymentStatus.SUCCEEDED);
        payment.getBooking().setStatus(BookingStatus.PAID);
        paymentRepository.save(payment);
        sendPaymentSuccessEvent(payment);
    }

    public Payment getValidPaymentForConfirmation(String username, ConfirmPaymentRequest request){
        UUID paymentId = UUID.fromString(request.getExternalId());

        Payment payment = paymentRepository.findByExternalId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (!payment.getUser().getUsername().equals(username)) {
            throw new BadRequestException();
        }

        if (!payment.getConfirmationCode().equals(request.getCode())) {
            throw new BadRequestException();
        }

        validatePaymentStatus(payment);

        if (payment.getCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ConfirmationCodeExpiredException();
        }

        return payment;
    }

    private void validatePaymentStatus(Payment payment){
        switch (payment.getPaymentStatus()){
            case SUCCEEDED:
                throw new PaymentAlreadyConfirmedException("Payment already confirmed");
            case CANCELLED:
                throw new PaymentAlreadyCanceledException("Payment was cancelled");
            case FAILED:
                throw new PaymentAlreadyFailedException("Payment failed previously");
            case PENDING:
                break;
            default:
                throw new BadRequestException();
        }
    }

    @Transactional
    public void resendConfirmationCode(String username, UUID paymentId){
        Payment payment = paymentRepository.findByExternalIdAndUserUsername(paymentId, username)
                .orElseThrow(PaymentNotFoundException::new);

        if(payment.getPaymentStatus() != PaymentStatus.PENDING){
            throw new BadRequestException();
        }

        String newCode = CodeGenerator.generateCode();
        payment.setConfirmationCode(newCode);
        payment.setCodeExpiresAt(CodeGenerator.generateExpiryTime());

        paymentRepository.save(payment);
        paymentFactoryService.sendConfirmationCode(
                payment.getUser(),
                newCode,
                payment
        );
    }

    private void sendPaymentSuccessEvent(Payment payment){
        Route route = payment.getBooking().getRoute();
        String routeInfo = route.getRouteFrom() + " - " +
                route.getRouteTo() + "; " +
                route.getDestinationTime() + "; " +
                route.getArrivalTime() + ";" +
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
}
