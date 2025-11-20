package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.payment.ConfirmPaymentRequest;
import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.exceptions.booking.BookingCancelledException;
import com.example.transport_marketplace.exceptions.booking.BookingDoesNotBelongUserException;
import com.example.transport_marketplace.exceptions.booking.BookingNotFoundException;
import com.example.transport_marketplace.exceptions.booking.BookingPaidException;
import com.example.transport_marketplace.exceptions.payment.ConfirmationCodeExpiredException;
import com.example.transport_marketplace.exceptions.payment.PaymentAlreadyCanceledException;
import com.example.transport_marketplace.exceptions.payment.PaymentAlreadyConfirmedException;
import com.example.transport_marketplace.exceptions.payment.PaymentAlreadyFailedException;
import com.example.transport_marketplace.exceptions.user.UserHasNoEmailException;
import com.example.transport_marketplace.service.PaymentConfirmationService;
import com.example.transport_marketplace.service.PaymentFactoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private final PaymentFactoryService paymentFactoryService;
    @Autowired
    private final PaymentConfirmationService paymentConfirmationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-info")
    public ResponseEntity<?> preparePayment(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam int bookingId
    ){
        try{
            return ResponseEntity.ok(paymentFactoryService.prepareOrder(userDetails.getUsername(), bookingId));
        }
        catch (BookingDoesNotBelongUserException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        catch (BookingNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Booking was not found");
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer bookingId,
            @RequestParam PaymentMethod paymentMethod
    ) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(paymentFactoryService.createPayment(userDetails.getUsername(), bookingId, paymentMethod));
        }
        catch (BookingNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        catch (UserHasNoEmailException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        catch(BookingCancelledException | BookingPaidException e){
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendCodeConfirmation(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID paymentId
    ) {
        try{
            paymentConfirmationService.resendConfirmationCode(userDetails.getUsername(), paymentId);
            return ResponseEntity.ok("Code was sent to user's email");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ConfirmPaymentRequest request
            ) {
        try {
            paymentConfirmationService.confirmPayment(userDetails.getUsername(), request);
            return ResponseEntity.ok("Payment was successfully confirmed!");
        }
        catch (PaymentAlreadyConfirmedException e){
            return ResponseEntity.ok("CONFIRMED");
        }
        catch (PaymentAlreadyCanceledException | PaymentAlreadyFailedException e){
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(e.getMessage());
        }
        catch (ConfirmationCodeExpiredException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam String externalId
    ) {
        try{
            if(!paymentFactoryService.cancelPayment(externalId)){
                return ResponseEntity.ok("Payment is already canceled!");
            }
            return ResponseEntity.ok("Payment was successfully canceled!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-my")
    public ResponseEntity<?> getMyPayments(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable
    ) {
        try {
            return ResponseEntity.ok(paymentFactoryService.getMyPayments(
                    userDetails.getUsername(),
                    pageable
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public ResponseEntity<?> getHistoryOfBooking(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Integer bookingId,
            Pageable pageable
    ) {
        try{
            return ResponseEntity.ok(paymentFactoryService.getHistoryByBookingId(
                    userDetails.getUsername(),
                    bookingId,
                    pageable
            ));
        }
        catch(BookingDoesNotBelongUserException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось просмотреть историю платежей!");
        }
    }
}
