package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.user = :user AND t.device = :device")
    void deleteByUserAndDevice(@Param("user") User user, @Param("device")Device device);


    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.user = :user")
    void deleteByUser(User user);
}