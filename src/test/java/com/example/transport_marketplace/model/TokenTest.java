package com.example.transport_marketplace.model;

import static org.junit.jupiter.api.Assertions.*;

import com.example.transport_marketplace.enums.Role;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class TokenTest {

    @Test
    void testTokenCreation_AllArgsConstructor() {
        Instant expiry = Instant.now().plusSeconds(3600);
        User user = new User(1, "testUser", "password123", Role.ROLE_USER);
        Token token = new Token(1, "sample-token", expiry, user);

        assertEquals(1, token.getId());
        assertEquals("sample-token", token.getToken());
        assertEquals(expiry, token.getExpiryDate());
        assertEquals(user, token.getUser());
    }

    @Test
    void testTokenBuilder() {
        Instant expiry = Instant.now().plusSeconds(7200);
        User user = new User(2, "builderUser", "builderPass", Role.ROLE_ADMIN);
        Token token = Token.builder()
                .id(2)
                .token("builder-token")
                .expiryDate(expiry)
                .user(user)
                .build();

        assertEquals(2, token.getId());
        assertEquals("builder-token", token.getToken());
        assertEquals(expiry, token.getExpiryDate());
        assertEquals(user, token.getUser());
    }

    @Test
    void testTokenSettersAndGetters() {
        Token token = new Token();
        Instant expiry = Instant.now().plusSeconds(1800);
        User user = new User();
        user.setId(3);
        user.setUsername("setterUser");
        user.setPassword("setterPass");
        user.setRole(Role.ROLE_USER);

        token.setId(3);
        token.setToken("setter-token");
        token.setExpiryDate(expiry);
        token.setUser(user);

        assertEquals(3, token.getId());
        assertEquals("setter-token", token.getToken());
        assertEquals(expiry, token.getExpiryDate());
        assertEquals(user, token.getUser());
    }
}
