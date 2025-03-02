package com.example.transport_marketplace.entity.tokens;

import com.example.transport_marketplace.authentication.JwtService;
import com.example.transport_marketplace.entity.users.User;
import com.example.transport_marketplace.entity.users.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtService jwtService;


    public RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(jwtService.generateRefreshToken(user));
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()));
        return refreshTokenRepository.save(refreshToken);
    }
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
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
