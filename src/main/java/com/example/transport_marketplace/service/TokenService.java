package com.example.transport_marketplace.service;

import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.repo.RefreshTokenRepository;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtService jwtService;


    public Token createRefreshToken(User user){
        refreshTokenRepository.deleteByUser(user);
        String token = jwtService.generateRefreshToken(user);
        Token refreshToken = Token.builder()
                .token(token)
                .user(user)
                .expiryDate(Instant.now().plus(7, ChronoUnit.DAYS))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
    public Optional<Token> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public Token verifyExpiration(Token token){
        if(token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token истек:(");
        }
        return token;
    }
    @Transactional
    public void deleteByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }

//    public void deleteByUserId(int userId){
//        refreshTokenRepository.deleteByUserId(userId);
//    }
}
