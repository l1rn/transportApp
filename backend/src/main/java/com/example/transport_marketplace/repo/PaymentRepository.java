package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}
