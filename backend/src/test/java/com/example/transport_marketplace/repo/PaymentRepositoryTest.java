package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Payment;
import com.example.transport_marketplace.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentRepositoryTest {
    @Mock
    private PaymentRepository paymentRepository;

    private User user;
    private Booking booking;
    private Payment testPayment;

    @BeforeEach
    public void setUp() {
        booking = Booking.builder()

                .build();

        testPayment = Payment.builder()
                .id(1)
                .externalId(UUID.randomUUID())
                .amount(1000.00)
                .paymentMethod(PaymentMethod.BANK_TRANSFER)
                .paymentStatus(PaymentStatus.CANCELLED)
                .description("Moscow to Saint Petersburg")
                .confirmationCode("123456")
                .codeExpiresAt(LocalDateTime.now().plusMinutes(1))
                .build();
    }

    public void givenPayment_whenSaved_thenCanBeFoundById() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(paymentRepository.findById(testPayment.getId())).thenReturn(Optional.of(testPayment));
        when(paymentRepository.findByExternalId(testPayment.getExternalId())).thenReturn(Optional.of(testPayment));

        Payment foundPayment = paymentRepository.findById(testPayment.getId())
                .orElse(null);

        assertNotNull(foundPayment);
    }

    @AfterEach
    public void tearDown() {
        reset(paymentRepository);
    }
}
