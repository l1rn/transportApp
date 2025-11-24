package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserId(int userId);
    List<Booking> findByUser(User user);
    @Query("SELECT b FROM Booking b")
    Page<Booking> findAllPageable(Pageable pageable);
    @Modifying
    @Transactional
    @Query("DELETE FROM Booking b WHERE b.user.id = :userId")
    void deleteByUserId(@Param("userId") int userId);
    boolean existsByUserAndRoute(User user, Route route);
    @Modifying
    @Transactional
    @Query("DELETE FROM Booking b WHERE b.status = :status")
    void deleteByStatus(@Param("status") BookingStatus status);
    boolean existsByUserIdAndId(Integer userId, Integer bookingId);
}