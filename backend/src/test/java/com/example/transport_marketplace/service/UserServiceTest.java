package com.example.transport_marketplace.service;

import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = TestFixtures.createTestUser();
    }
    
    @Test
    public void testFindUserById_CompareResultsRepositoryAndService() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User foundUserService = userService.getById(testUser.getId());

        assertNotNull(foundUserService);
        assertEquals(testUser.getUsername(), foundUserService.getUsername());

        verify(userRepository).findById(testUser.getId());
    }
}
