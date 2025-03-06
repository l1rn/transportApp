package com.example.transport_marketplace.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        User user = new User(1, "testUser", "password123", Role.ROLE_USER);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_Success() {
        User user = new User(1, "newUser", "password123", Role.ROLE_USER);
        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(user);

        assertNotNull(result);
        assertEquals("newUser", result.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_UsernameTaken() {
        User user = new User(1, "existingUser", "password123", Role.ROLE_USER);
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.create(user));
        assertEquals("Имя пользователя занято!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(
                new User(1, "user1", "password1", Role.ROLE_USER),
                new User(2, "user2", "password2", Role.ROLE_ADMIN)
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetByUsername_Success() {
        User user = new User(1, "testUser", "password123", Role.ROLE_USER);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        User result = userService.getByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

    @Test
    void testGetByUsername_NotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.getByUsername("unknownUser"));
        assertEquals("User not found: unknownUser", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknownUser");
    }
}
