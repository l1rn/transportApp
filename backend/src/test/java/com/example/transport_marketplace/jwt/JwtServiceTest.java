package com.example.transport_marketplace.jwt;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        String signingKey = "MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTI=";
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", signingKey);
        ReflectionTestUtils.setField(jwtService, "accessExpirationMs", 3600000L);    // 1 час
        ReflectionTestUtils.setField(jwtService, "refreshExpirationMs", 86400000L);  // 1 день
    }

    @Test
    void testGenerateAccessToken() {
        User user = new User(1, "testuser", "password", Role.ROLE_USER);
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);

        String username = jwtService.getUsernameFromToken(token);
        assertEquals("testuser", username);

        String role = jwtService.getRoleFromToken(token);
        assertEquals("ROLE_USER", role);

        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testGenerateRefreshToken() {
        User user = new User(1, "testuser", "password", Role.ROLE_USER);
        String token = jwtService.generateRefreshToken(user);
        assertNotNull(token);
        assertEquals("testuser", jwtService.getUsernameFromToken(token));
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testValidateToken_Invalid() {
        assertFalse(jwtService.validateToken("invalid.token.here"));
    }
}
