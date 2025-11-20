package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.enums.PaymentStatus;
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
    Optional<Payment> findByBookingIdAndPaymentStatus(
            Integer bookingId,
            PaymentStatus status
    );

    Optional<Payment> findByExternalId(UUID externalId);

    @Query("SELECT p from Payment p WHERE p.user.id = :userId")
    Page<Payment> findAllByUserId(
            @Param("userId") Integer userId,
            Pageable pageable
    );

    @Query("SELECT p from Payment p WHERE p.booking.id = :bookingId")
    Page<Payment> findAllByBookingId (
            @Param("bookingId") Integer bookingId,
            Pageable pageable
    );
    Optional<Payment> findByExternalIdAndUserUsername(UUID externalId, String username);
    Optional<Payment> findFirstByBookingIdAndPaymentStatusInOrderByCreatedAtDesc(Integer bookingId, List<PaymentStatus> paymentStatusIn);
}
