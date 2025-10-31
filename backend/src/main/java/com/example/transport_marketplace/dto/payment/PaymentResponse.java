package com.example.transport_marketplace.dto.payment;

import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.model.Payment;
import com.example.transport_marketplace.model.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;


public record PaymentResponse (
    int id,
    double amount,
    PaymentMethod method,
    PaymentStatus status,
    String description,
    LocalDateTime createdAt,
    String username,
    RouteResponseForPayment route

) {
    public static PaymentResponse from(Payment payment){
        return new PaymentResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getDescription(),
                payment.getCreatedAt(),
                payment.getUser().getUsername(),
                RouteResponseForPayment.from(payment.getBooking().getRoute())
        );
    }
}
