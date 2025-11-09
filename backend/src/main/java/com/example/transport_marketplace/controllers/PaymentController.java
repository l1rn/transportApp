package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.payment.ConfirmPaymentRequest;
import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.exceptions.payment.PaymentAlreadyCanceledException;
import com.example.transport_marketplace.exceptions.payment.PaymentAlreadyConfirmedException;
import com.example.transport_marketplace.exceptions.payment.PaymentAlreadyFailedException;
import com.example.transport_marketplace.service.PaymentService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private final PaymentService paymentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-info")
    public ResponseEntity<?> preparePayment(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestParam int bookingId
    ){
        try{
            return ResponseEntity.ok(paymentService.prepareOrder(userDetails.getUsername(), bookingId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int bookingId,
            @RequestParam PaymentMethod paymentMethod
    ) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(paymentService.createPayment(userDetails.getUsername(), bookingId, paymentMethod));
        }
        catch(PaymentAlreadyCanceledException e){
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(e.getMessage());
        }
        catch (PaymentAlreadyFailedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ConfirmPaymentRequest request
            ) {
        try {
            paymentService.confirmPayment(userDetails.getUsername(), request);
            return ResponseEntity.ok("Payment was successfully confirmed!");
        }
        catch (PaymentAlreadyConfirmedException e){
            return ResponseEntity.ok("CONFIRMED");
        }
        catch (PaymentAlreadyFailedException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
        catch (PaymentAlreadyCanceledException e){
            return ResponseEntity.status(HttpStatus.GONE)
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
            if(!paymentService.cancelPayment(externalId)){
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
            return ResponseEntity.ok(paymentService.getMyPayments(
                    userDetails.getUsername(),
                    pageable
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history/bookingId")
    public ResponseEntity<?> getHistoryOfBooking(
            @RequestParam Integer bookingId,
            Pageable pageable
    ) {
        try{
            return ResponseEntity.ok(paymentService.getHistoryByBookingId(
                    bookingId,
                    pageable
            ));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось просмотреть историю платежей!");
        }
    }
}
