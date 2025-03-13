package com.example.transport_marketplace.config;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void initAdminUser() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("secure_admin"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
    @EventListener(ApplicationReadyEvent.class)
    public void initFirstUser() {
        if (!userRepository.existsByUsername("user")){
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user_password"))
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }
    }
}
