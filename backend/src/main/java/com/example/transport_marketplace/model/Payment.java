package com.example.transport_marketplace.model;

import com.example.transport_marketplace.enums.PaymentMethod;
import com.example.transport_marketplace.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "payments")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UUID externalId;
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String description;

    private String confirmationCode;
    private LocalDateTime codeExpiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
    }
}
