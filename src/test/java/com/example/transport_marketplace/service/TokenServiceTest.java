package com.example.transport_marketplace.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.RefreshTokenRepository;
import com.example.transport_marketplace.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TokenService tokenService;

    @Test
    void testCreateRefreshToken() {
        User user = new User(1, "testUser", "password", Role.ROLE_USER);
        String generatedToken = "refresh-token-123";
        long refreshExpirationMs = 600000L;

        when(jwtService.generateRefreshToken(user)).thenReturn(generatedToken);
        when(jwtService.getRefreshExpirationMs()).thenReturn(refreshExpirationMs);
        when(refreshTokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Token token = tokenService.createRefreshToken(user);

        assertNotNull(token);
        assertEquals(user, token.getUser());
        assertEquals(generatedToken, token.getToken());
        assertTrue(token.getExpiryDate().isAfter(Instant.now()));

        verify(refreshTokenRepository, times(1)).save(any(Token.class));
        verify(jwtService, times(1)).generateRefreshToken(user);
        verify(jwtService, times(1)).getRefreshExpirationMs();
    }

    @Test
    void testFindByToken() {
        String tokenStr = "test-token";
        User user = new User(1, "testUser", "password", Role.ROLE_USER);
        Token token = new Token(1, tokenStr, Instant.now().plusSeconds(60), user);

        when(refreshTokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        Optional<Token> foundToken = tokenService.findByToken(tokenStr);

        assertTrue(foundToken.isPresent());
        assertEquals(token, foundToken.get());
        verify(refreshTokenRepository, times(1)).findByToken(tokenStr);
    }

    @Test
    void testFindByUser() {
        User user = new User(1, "testUser", "password", Role.ROLE_USER);
        Token token = new Token(1, "test-token", Instant.now().plusSeconds(60), user);

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(token));

        Optional<Token> foundToken = tokenService.findByUser(user);

        assertTrue(foundToken.isPresent());
        assertEquals(token, foundToken.get());
        verify(refreshTokenRepository, times(1)).findByUser(user);
    }

    @Test
    void testVerifyExpiration_ValidToken() {
        User user = new User(1, "testUser", "password", Role.ROLE_USER);
        Token token = new Token(1, "valid-token", Instant.now().plusSeconds(60), user);

        // Для валидного токена метод должен вернуть сам токен без удаления
        Token verifiedToken = tokenService.verifyExpiration(token);
        assertEquals(token, verifiedToken);
        verify(refreshTokenRepository, never()).delete(token);
    }

    @Test
    void testVerifyExpiration_ExpiredToken() {
        User user = new User(1, "testUser", "password", Role.ROLE_USER);
        Token token = new Token(1, "expired-token", Instant.now().minusSeconds(60), user);

        doNothing().when(refreshTokenRepository).delete(token);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> tokenService.verifyExpiration(token));
        assertEquals("Refresh token истек:(", exception.getMessage());
        verify(refreshTokenRepository, times(1)).delete(token);
    }

    @Test
    void testDeleteByUser() {
        User user = new User(1, "testUser", "password", Role.ROLE_USER);

        doNothing().when(refreshTokenRepository).deleteByUser(user);

        tokenService.deleteByUser(user);

        verify(refreshTokenRepository, times(1)).deleteByUser(user);
    }
}
