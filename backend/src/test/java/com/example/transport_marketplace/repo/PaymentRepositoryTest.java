package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Payment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentRepositoryTest {
    @Mock
    private PaymentRepository paymentRepository;

    private Payment testPayment;

    @BeforeEach
    public void setUp() {
        testPayment = TestFixtures.createTestPayment();
    }

    @Test
    public void givenPayment_whenSaved_thenCanBeFoundById() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(paymentRepository.findById(testPayment.getId())).thenReturn(Optional.of(testPayment));
        when(paymentRepository.findByExternalId(testPayment.getExternalId())).thenReturn(Optional.of(testPayment));

        Payment savedPayment = paymentRepository.save(testPayment);
        Payment foundPayment = paymentRepository.findById(testPayment.getId())
                .orElse(null);

        assertNotNull(foundPayment);

        assertEquals(foundPayment.getPaymentStatus(), testPayment.getPaymentStatus());
        assertEquals(foundPayment.getDescription(), testPayment.getDescription());
        assertEquals(foundPayment.getAmount(), testPayment.getAmount());

        verify(paymentRepository).save(testPayment);
        verify(paymentRepository).findById(testPayment.getId());
    }

    @AfterEach
    public void tearDown() {
        reset(paymentRepository);
    }
}
