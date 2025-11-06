package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false,
    properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    })
public class DeviceRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    Device device1;
    Device device2;
    User user;
    @BeforeEach
    void buildModels(){
        user = User.builder()
                .username("test")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        device1 = Device.builder()
                .deviceFingerprint("312")
                .userAgent("win")
                .user(user)
                .build();
        device2 = Device.builder()
                .deviceFingerprint("123")
                .userAgent("mac")
                .user(user)
                .build();

        deviceRepository.save(device1);
        deviceRepository.save(device2);
    }

    @Test
    void testFindAllDevicesByUser(){
        user.setDevices(List.of(device1, device2));
        List<Device> findDevices = deviceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Не удалось найти устройства данного пользователя!"));
        assertIterableEquals(user.getDevices(), findDevices);
    }

//    @Test
//    void testFindByDeviceFingerprintAndUser(){
//        List<Device> findDevice = deviceRepository.findByDeviceFingerprintAndUser(device2.getDeviceFingerprint(), user);
//        assertTrue(findDevice.is());
//        assertEquals("mac", findDevice.get().getUserAgent());
//        assertEquals("test", findDevice.get().getUser().getUsername());
//    }

    @Test
    void testFindByUserAndUserAgent(){
        Optional<Device> findDevice = deviceRepository.findByUserAndUserAgent(user, device1.getUserAgent());
        assertTrue(findDevice.isPresent());
        assertEquals("win", findDevice.get().getUserAgent());
    }
}
