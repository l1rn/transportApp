package com.example.transport_marketplace.events;

import com.example.transport_marketplace.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessEvent {
    private int paymentId;
    private int bookingId;
    private double amount;
    private String userEmail;
    private String userName;
    private String routeNumber;
    private PaymentMethod paymentMethod;

    @Builder.Default
    private LocalDateTime paymentTime = LocalDateTime.now();
}
