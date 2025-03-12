package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.AuthController;
import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.dto.auth.LogoutRequest;
import com.example.transport_marketplace.dto.jwt.RefreshTokenRequest;
import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.auth.SignUpRequest;
import com.example.transport_marketplace.service.AuthenticationService;
import com.example.transport_marketplace.jwt.TokenBlacklist;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = { AuthController.class, AuthControllerTest.Config.class })
@Import(AuthControllerTest.Config.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class Config {
        @Bean
        public AuthenticationService authenticationService() {
            return Mockito.mock(AuthenticationService.class);
        }

        @Bean
        public TokenBlacklist tokenBlacklist() {
            return Mockito.mock(TokenBlacklist.class);
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    @Test
    void testSignUp() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("newUser");
        request.setPassword("password123");

        doNothing().when(authenticationService).signUp(any(SignUpRequest.class));

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь зарегистрирован"));

        Mockito.verify(authenticationService).signUp(any(SignUpRequest.class));
    }

    @Test
    void testSignIn() throws Exception {
        SignInRequest request = new SignInRequest();
        request.setUsername("user1");
        request.setPassword("pass1");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("access-token", "refresh-token");

        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));

        Mockito.verify(authenticationService).signIn(any(SignInRequest.class));
    }

    @Test
    void testRefreshToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("old-refresh-token");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("new-access-token", "new-refresh-token");

        when(authenticationService.refreshToken("old-refresh-token")).thenReturn(response);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

        Mockito.verify(authenticationService).refreshToken("old-refresh-token");
    }

    @Test
    void testLogout() throws Exception {
        LogoutRequest request = new LogoutRequest();
        request.setRefreshToken("refresh-token");

        doNothing().when(tokenBlacklist).revoke("access-token");
        doNothing().when(authenticationService).deleteTokenByUser("refresh-token");

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer access-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Mockito.verify(tokenBlacklist).revoke("access-token");
        Mockito.verify(authenticationService).deleteTokenByUser("refresh-token");
    }
}
