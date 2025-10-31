package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByExternalId(UUID externalId);
    @Query("SELECT p from Payment p WHERE p.user.id = :userId")
    Page<Payment> findAllByUserId(@Param("userId") int userId, Pageable pageable);
}
