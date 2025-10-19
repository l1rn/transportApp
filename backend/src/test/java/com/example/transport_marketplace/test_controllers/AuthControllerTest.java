
package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.AuthController;
import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.dto.jwt.RefreshTokenRequest;
import com.example.transport_marketplace.dto.auth.SignUpRequest;
import com.example.transport_marketplace.jwt.JwtAuthenticationFilter;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.AuthenticationService;
import com.example.transport_marketplace.jwt.TokenBlacklist;
import com.example.transport_marketplace.service.impl.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklist tokenBlacklist;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthController authController;

    private Cookie accessCookie;
    private Cookie refreshCookie;

    @BeforeEach
    void setupAndSignIn() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .addFilter(new JwtAuthenticationFilter(jwtService, userDetailsService, tokenBlacklist))
                .defaultRequest(get("/").with(csrf()).characterEncoding(StandardCharsets.UTF_8))
                .build();

        SignInRequest request = new SignInRequest();
        request.setUsername("1");
        request.setPassword("1");
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setAccessToken("accessToken");
        response.setRefreshToken("refreshToken");

        when(authenticationService.signIn(any(SignInRequest.class), any(HttpServletRequest.class)))
                .thenReturn(response);

        ResultActions signInResult = mockMvc.perform(post("/api/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Authorized"));

        accessCookie = signInResult.andReturn().getResponse().getCookie("accessToken");
        refreshCookie = signInResult.andReturn().getResponse().getCookie("refreshToken");

        assertNotNull(accessCookie);
        assertNotNull(refreshCookie);
    }

    @Test
    void testSignUp() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("newUser");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Success registration"));
    }


    @Test
    void testRefreshToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("old-refresh-token");

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setRefreshToken("refreshToken");
        jwtAuthenticationResponse.setAccessToken("accessToken");

        when(authenticationService.refreshToken(request.getRefreshToken())).thenReturn(jwtAuthenticationResponse);

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("refreshToken", request.getRefreshToken()))
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Mockito.verify(authenticationService).refreshToken("old-refresh-token");
    }

    @Test
    void testLogout() throws Exception {
        when(jwtService.validateToken("accessToken")).thenReturn(true);
        when(jwtService.getUsernameFromToken("accessToken")).thenReturn("1");
        when(userDetailsService.loadUserByUsername("1")).thenReturn(mock(User.class));

        MvcResult logoutResult = mockMvc.perform(post("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(accessCookie, refreshCookie))
                .andExpect(status().isNoContent())
                .andReturn();

        Cookie clearAccessCookie = logoutResult.getResponse().getCookie("accessToken");
        Cookie clearRefreshCookie = logoutResult.getResponse().getCookie("refreshToken");

        assertEquals(0, clearAccessCookie.getMaxAge());
        assertEquals(0, clearRefreshCookie.getMaxAge());
    }
}
