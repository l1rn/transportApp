package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByExternalId(UUID externalId);
}
