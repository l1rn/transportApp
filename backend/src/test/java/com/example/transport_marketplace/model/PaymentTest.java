package com.example.transport_marketplace.model;

import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentTest {
    @Test
    void testPaymentCreation() {
        Booking booking = Booking.builder()
                .build();
        User user = User.builder()
                .username("test")
                .password("123")
                .build();
        UUID externalId = UUID.randomUUID();

        Payment payment = Payment.builder()
                .externalId(externalId)
                .amount(1000.00)
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .paymentStatus(PaymentStatus.REFUNDED)
                .description("123")
                .confirmationCode("123456")
                .codeExpiresAt(LocalDateTime.now().plusMinutes(1))
                .booking(booking)
                .user(user)
                .build();

        assertEquals(externalId, payment.getExternalId());
        assertEquals(1000.00, payment.getAmount());
        assertEquals(PaymentMethod.BANK_TRANSFER, payment.getPaymentMethod());
        assertEquals(PaymentStatus.REFUNDED, payment.getPaymentStatus());
        assertEquals("123", payment.getDescription());
        assertEquals("123456", payment.getConfirmationCode());
    }
}
