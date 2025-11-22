package com.example.transport_marketplace.service;

import com.example.transport_marketplace.config.CodeGenerator;
import com.example.transport_marketplace.dto.PaginatedResponse;
import com.example.transport_marketplace.dto.payment.PaymentResponse;
import com.example.transport_marketplace.dto.payment.PreparationOrderResponse;
import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.events.ConfirmationCodeEvent;
import com.example.transport_marketplace.exceptions.booking.BookingDoesNotBelongUserException;
import com.example.transport_marketplace.exceptions.booking.BookingNotFoundException;
import com.example.transport_marketplace.model.*;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.PaymentRepository;
import com.example.transport_marketplace.repo.UserRepository;
import com.example.transport_marketplace.service.validators.PaymentValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentFactoryService {
    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final PaymentValidator paymentValidator;

    public PreparationOrderResponse prepareOrder(String username, Integer bookingId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User was not found by this token"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Cannot found the booking by id#" + bookingId));

        if(booking.getUser() != user){
            throw new BookingDoesNotBelongUserException("Этот букинг не принадлежит этому юзеру!");
        }

        return PreparationOrderResponse.builder()
                .routeId(booking.getRoute().getId())
                .orderFullName(
                        booking.getRoute().getRouteFrom() + " → " +
                        booking.getRoute().getRouteTo()
                )
                .paymentMethods(Arrays.stream(PaymentMethod.values()).toList())
                .price(booking.getRoute().getPrice())
                .hasEmail(user.getEmail() != null)
                .inProgress(paymentValidator.checkPaymentPendingByBooking(booking))
                .paid(booking.getStatus() == BookingStatus.PAID)
                .build();
    }

    public UUID getExternalId(String username, Integer bookingId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не удалось найти пользователя: " + username));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking was not found by this id"));
        if(user != booking.getUser()){
            throw new BookingDoesNotBelongUserException("Этот заказ не принадлежит этому юзеру!");
        }

        Optional<Payment> payment = paymentRepository
                .findByBookingIdAndPaymentStatus(booking.getId(), PaymentStatus.PENDING);
        return payment.map(Payment::getExternalId).orElse(null);
    }

    public UUID createPayment(String username, Integer bookingId, PaymentMethod method) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не удалось найти пользователя: " + username));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking was not found by this id"));
        paymentValidator.validatePaymentEligibility(user, booking);
        return handlePendingPaymentOrCreateNew(user, booking, method);
    }

    private UUID handlePendingPaymentOrCreateNew(User user, Booking booking, PaymentMethod method){
        Optional<Payment> pendingOpt = paymentRepository
                .findByBookingIdAndPaymentStatus(booking.getId(), PaymentStatus.PENDING);

        if(pendingOpt.isPresent()){
            return handleExistingPendingPayment(pendingOpt.get(), user);
        }

        return createNewPayment(user, booking, method).getExternalId();
    }

    private UUID handleExistingPendingPayment(Payment payment, User user) {
        if (payment.getCodeExpiresAt().isAfter(LocalDateTime.now())) {
            return payment.getExternalId();
        }

        String newCode = CodeGenerator.generateCode();
        LocalDateTime newExpiry = CodeGenerator.generateExpiryTime();

        payment.setConfirmationCode(newCode);
        payment.setCodeExpiresAt(newExpiry);

        paymentRepository.save(payment);
        sendConfirmationCode(user, newCode, payment);

        return payment.getExternalId();
    }

    private Payment createNewPayment(User user, Booking booking, PaymentMethod method){
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

        paymentRepository.save(payment);
        sendConfirmationCode(user, storedCode, payment);
        return payment;
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

    @Transactional
    public boolean cancelPayment(String externalId){
        Payment payment = paymentRepository.findByExternalId(UUID.fromString(externalId))
                .orElseThrow(() -> new RuntimeException("Платеж не найден"));

        if(payment.getPaymentStatus() == PaymentStatus.CANCELLED) {
            return false;
        }

        Booking booking = payment.getBooking();
        booking.setStatus(BookingStatus.CANCELLED);
        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        bookingRepository.save(booking);
        paymentRepository.save(payment);
        return true;
    }

    public PaginatedResponse<PaymentResponse> getMyPayments(
            String username,
            Pageable pageable
    ){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Данный пользователь не был найден!"));

        Page<Payment> payments = paymentRepository.findAllByUserId(user.getId(), pageable);

        List<PaymentResponse> content = payments.stream()
                .map(PaymentResponse::from)
                .toList();

        return new PaginatedResponse<>(
                content,
                payments.getNumber(),
                payments.getSize(),
                payments.getTotalElements(),
                payments.getTotalPages()
        );
    }

    public PaginatedResponse<PaymentResponse> getHistoryByBookingId(String username, Integer bookingId, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Данный пользователь не был найден!"));
        if(!bookingRepository.existsByUserIdAndId(user.getId(), bookingId)){
            throw new BookingDoesNotBelongUserException("Этот букинг не принадлежит вам!");
        }

        Page<Payment> payments = paymentRepository.findAllByBookingId(bookingId, pageable);

        List<PaymentResponse> content = payments.stream()
                .map(PaymentResponse::from)
                .toList();

        return new PaginatedResponse<>(
                content,
                payments.getNumber(),
                payments.getSize(),
                payments.getTotalElements(),
                payments.getTotalPages()
        );
    }
}
