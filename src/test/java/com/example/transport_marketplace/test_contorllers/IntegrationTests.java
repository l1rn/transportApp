package com.example.transport_marketplace.test_contorllers;

import com.example.transport_marketplace.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private static String accessToken;
    private static String refreshToken;
    private static int bookingId;

    // Тестовые данные
    private final SignUpRequest signUpRequest = new SignUpRequest("testuser", "password");
    private final SignInRequest signInRequest = new SignInRequest("testuser", "password");

    @Test
    @Order(1)
    void testUserRegistration() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/signup",
                signUpRequest,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered", response.getBody());
    }

    @Test
    @Order(2)
    void testUserLogin() {
        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity(
                "/auth/signin",
                signInRequest,
                JwtAuthenticationResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getAccessToken());
        assertNotNull(response.getBody().getRefreshToken());

        accessToken = response.getBody().getAccessToken();
        refreshToken = response.getBody().getRefreshToken();
    }


    @Test
    @Order(4)
    void testRefreshToken() {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest(refreshToken);

        ResponseEntity<JwtAuthenticationResponse> response = restTemplate.postForEntity(
                "/auth/refresh",
                refreshRequest,
                JwtAuthenticationResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(accessToken, response.getBody().getAccessToken());

        accessToken = response.getBody().getAccessToken();
    }


    @Test
    @Order(5)
    void testLogout() {
        LogoutRequest logoutRequest = new LogoutRequest(accessToken, refreshToken);

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/auth/logout",
                logoutRequest,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(6)
    void testAccessAfterLogout() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<String> response = restTemplate.exchange(
                "/profile/bookings/my",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(7)
    void testInvalidCredentials() {
        SignInRequest invalidRequest = new SignInRequest("testuser", "wrongpassword");

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/signin",
                invalidRequest,
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Order(8)
    void testDuplicateRegistration() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/auth/signup",
                signUpRequest,
                String.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}