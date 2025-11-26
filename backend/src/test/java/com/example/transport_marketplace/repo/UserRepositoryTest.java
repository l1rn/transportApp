package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User testUser;
    @BeforeEach
    public void setUp(){
        testUser = TestFixtures.createTestUser();
    }

    @Test
    void givenUser_whenSaved_thenCanBeFoundById(){
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        User savedUser = userRepository.save(testUser);
        User foundUser = userRepository.findById(testUser.getId())
                .orElse(null);

        assertNotNull(foundUser);
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        assertEquals(testUser.getPassword(), foundUser.getPassword());

        verify(userRepository).save(testUser);
        verify(userRepository).findById(testUser.getId());
    }
    
    @AfterEach
    public void tearDown() throws Exception{
        reset(userRepository);
    }
}
