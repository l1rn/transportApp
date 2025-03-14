package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.UsersController;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.JwtAuthenticationFilter;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.jwt.TokenBlacklist;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.UserService;
import com.example.transport_marketplace.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklist tokenBlacklist;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private UsersController usersController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(usersController)
                .addFilter(new JwtAuthenticationFilter(jwtService, userDetailsService, tokenBlacklist))
                .defaultRequest(get("/").with(csrf()))
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturnUsersList() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setRole(Role.ROLE_ADMIN);
        List<User> users = Collections.singletonList(user);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    @WithMockUser
    void getCurrentUserRole_ShouldReturnUserRole() throws Exception {
        User user = new User();
        user.setRole(Role.ROLE_USER);

        when(userService.getCurrentUser()).thenReturn(user);

        mockMvc.perform(get("/api/users/me/role"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

}
