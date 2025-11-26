package com.example.transport_marketplace.jwt;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        String signingKey = "MTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTI=";
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", signingKey);
        ReflectionTestUtils.setField(jwtService, "accessExpirationMs", 360000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpirationMs", 86400000L);
    }

    @Test
    void testGenerateAccessToken() {
        Device device = Device.builder()
                .deviceFingerprint("123")
                .userAgent("win")
                .build();
        User user = User.builder()
                .id(1)
                .username("testUser")
                .password("password123")
                .role(Role.ROLE_USER)
                .devices(List.of(device))
                .build();
        String token = jwtService.generateAccessToken(user);
        assertNotNull(token);

        String username = jwtService.getUsernameFromToken(token);
        assertEquals("testUser", username);

        String role = jwtService.getRoleFromToken(token);
        assertEquals("ROLE_USER", role);

        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testGenerateRefreshToken() {
        Device device = Device.builder()
                .deviceFingerprint("123")
                .userAgent("win")
                .build();
        User user = User.builder()
                .id(1)
                .username("testUser")
                .password("password123")
                .role(Role.ROLE_USER)
                .devices(List.of(device))
                .build();
        String token = jwtService.generateRefreshToken(user);
        assertNotNull(token);
        assertEquals("testUser", jwtService.getUsernameFromToken(token));
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void testValidateToken_Invalid() {
        assertFalse(jwtService.validateToken("invalid.token.here"));
    }
}
