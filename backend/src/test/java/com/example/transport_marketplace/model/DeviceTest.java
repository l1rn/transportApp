package com.example.transport_marketplace.model;

import com.example.transport_marketplace.enums.Role;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceTest {

    @Test
    void testDeviceBuilder(){
        User user = User.builder()
                .id(1)
                .password("123")
                .role(Role.ROLE_USER)
                .build();

        Device device = Device.builder()
                .id(1)
                .deviceFingerprint("finger")
                .userAgent("win")
                .build();

        Device device1 = Device.builder()
                .id(2)
                .deviceFingerprint("finger1")
                .userAgent("win1")
                .build();

        List<Device> list = new ArrayList<>();

        list.add(device);
        list.add(device1);

        device.setUser(user);
        user.setDevices(list);
        assertEquals(1, device.getId());
        assertEquals(2, device1.getId());
        assertEquals("win", device.getUserAgent());
        assertEquals("win1", device1.getUserAgent());
        assertEquals("finger", device.getDeviceFingerprint());
        assertEquals("finger1", device1.getDeviceFingerprint());
        assertEquals(list, user.getDevices());
    }
}
