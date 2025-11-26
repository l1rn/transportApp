package com.example.transport_marketplace.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationCodeEvent {
    private String userEmail;
    private String userName;
    private String confirmationCode;
    private int paymentId;
    private double amount;
    private LocalDateTime expiresAt;
}