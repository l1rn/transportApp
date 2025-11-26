package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.PaginatedResponse;
import com.example.transport_marketplace.dto.payment.PaymentResponse;
import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.Payment;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.PaymentRepository;
import com.example.transport_marketplace.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentFactoryService paymentFactoryService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    private User testUser;
    private Payment testPayment;

    @BeforeEach
    public void setUp(){
        testUser = TestFixtures.createTestUser();
        testPayment = TestFixtures.createTestPayment();
    }

    @Test
    void getPaymentsByUsername_shouldReturnPayments_whenExists(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Payment> paymentPage = new PageImpl<>(List.of(testPayment), pageable, 1);

        when(userRepository.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));
        when(paymentRepository.findAllByUserId(testUser.getId(), pageable))
                .thenReturn(paymentPage);

        PaginatedResponse<PaymentResponse> response = paymentFactoryService
                .getMyPayments(testUser.getUsername(), pageable);
        assertNotNull(response);
        assertEquals(1, response.content().size());
        assertEquals(testPayment.getId(), response.content().getFirst().id());
        assertEquals(testPayment.getAmount(), response.content().getFirst().amount());
        assertEquals(testPayment.getDescription(), response.content().getFirst().description());

        verify(userRepository, times(1)).findByUsername(testUser.getUsername());
        verify(paymentRepository, times(1)).findAllByUserId(testUser.getId(), pageable);
    }

    @AfterEach
    public void tearDown(){
        reset(paymentRepository, userRepository);
    }
}