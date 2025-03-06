package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserId(int userId);
    List<Booking> findByUser(User user);
}