package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);
    Optional<Token> findByUser(User user);
    void deleteByUser(User user);
    void deleteByUserId(int userId);
}