package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(showSql = false,
        properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
        })
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void testSaveAndFindByUsername(){

        Device device = Device.builder()
                .deviceFingerprint("finger")
                .userAgent("win")
                .build();

        User user = User.builder()
                .username("testuser")
                .password("123")
                .role(Role.ROLE_USER)
                .build();

        device.setUser(user);
        user.setDevices(List.of(device));
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals(Role.ROLE_USER, user.getRole());
    }
}
