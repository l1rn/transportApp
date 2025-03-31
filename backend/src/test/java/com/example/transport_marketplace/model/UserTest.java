package com.example.transport_marketplace.model;

import com.example.transport_marketplace.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void testUserCreation() {
        Device win = Device.builder()
                .id(1)
                .deviceFingerprint("finger-win")
                .userAgent("win")
                .build();
        Device mac = Device.builder()
                .id(1)
                .deviceFingerprint("finger-mac")
                .userAgent("mac")
                .build();

        List<Device> devices = new ArrayList<>();
        devices.add(win);
        devices.add(mac);

        User user = new User(1, "testUser", "password123", Role.ROLE_USER, devices);

        assertEquals(1, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(Role.ROLE_USER, user.getRole());
        assertEquals(devices, user.getDevices());
    }
}