package com.example.transport_marketplace.model;

import com.example.transport_marketplace.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User(1, "testUser", "password123", Role.ROLE_USER);

        assertEquals(1, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.ROLE_USER, user.getRole());
    }

}