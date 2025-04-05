package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.auth.SignUpRequest;
import com.example.transport_marketplace.dto.users.ChangePasswordRequest;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.DeviceRepository;
import com.example.transport_marketplace.repo.RefreshTokenRepository;
import com.example.transport_marketplace.repo.UserRepository;
import com.example.transport_marketplace.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private AuthenticationService authenticationService;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testSignUp_Success() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("newUser");
        request.setPassword("password123");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> authenticationService.signUp(request));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignUp_UserExists() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("existingUser");
        request.setPassword("password123");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.signUp(request));
        assertEquals("Такой пользователь уже есть", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testSignIn_Success() {
        SignInRequest request = new SignInRequest();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        Device device = Device.
                builder().id(1).deviceFingerprint("finger").userAgent("win").build();
        List<Device> devices = new ArrayList<>();
        request.setUsername("user1");
        request.setPassword("pass1");
        devices.add(device);
        User user = new User(1, "user1", "encodedPass", Role.ROLE_USER, devices);
        device.setUser(user);

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(deviceRepository.save(any(Device.class))).thenAnswer(inv -> inv.getArgument(0));

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
                );
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        doNothing().when(refreshTokenRepository).deleteByUser(user);

        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refresh-token");
        when(jwtService.getRefreshExpirationMs()).thenReturn(604800L);
        when(refreshTokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Token mockToken = mock(Token.class);
        when(refreshTokenRepository.save(any(Token.class))).thenReturn(mockToken);

        JwtAuthenticationResponse response = authenticationService.signIn(request, mockRequest);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByUsername("user1");
        verify(refreshTokenRepository).deleteByUserAndDevice(any(User.class), any(Device.class));
    }

    @Test
    void testRefreshToken_Success() {
        String oldRefreshToken = "old-refresh-token";
        User user = new User(1, "user1", "encodedPass", Role.ROLE_USER, null);
        Token storedToken = Token.builder()
                .token(oldRefreshToken)
                .user(user)
                .expiryDate(Instant.now().plusSeconds(60))
                .build();

        when(jwtService.validateToken(oldRefreshToken)).thenReturn(true);
        when(jwtService.getUsernameFromToken(oldRefreshToken)).thenReturn("user1");
        when(refreshTokenRepository.findByToken(oldRefreshToken)).thenReturn(Optional.of(storedToken));
        doNothing().when(refreshTokenRepository).delete(storedToken);
        when(jwtService.generateAccessToken(user)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(user)).thenReturn("new-refresh-token");
        when(jwtService.getRefreshExpirationMs()).thenReturn(60000L);
        when(refreshTokenRepository.save(any(Token.class))).thenAnswer(invocation -> invocation.getArgument(0));

        JwtAuthenticationResponse response = authenticationService.refreshToken(oldRefreshToken);

        assertNotNull(response);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());

        verify(refreshTokenRepository, times(1)).delete(storedToken);
        verify(refreshTokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testRefreshToken_InvalidToken() {
        String invalidToken = "invalid-token";
        when(jwtService.validateToken(invalidToken)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticationService.refreshToken(invalidToken));
        assertEquals("Invalid refresh token", exception.getMessage());
        verify(refreshTokenRepository, never()).findByToken(anyString());
    }

    @Test
    void testDeleteTokenByUser() {
        String refreshToken = "refresh-token";
        User user = new User(1, "user1", "encodedPass", Role.ROLE_USER, null);
        Token token = Token.builder().token(refreshToken).user(user).build();

        when(refreshTokenRepository.findByToken(refreshToken)).thenReturn(Optional.of(token));

        authenticationService.deleteRefreshToken(refreshToken);

        verify(refreshTokenRepository, times(1)).delete(token);
    }

    @Test
    void testChangePasswordByUsername_Success(){
        User user = User.builder()
                .username("test")
                .password(passwordEncoder.encode("123"))
                .role(Role.ROLE_USER)
                .build();
        userRepository.save(user);

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("123");
        request.setNewPassword("111");

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<User> findUser = userRepository.findByUsername(user.getUsername());
        User updateUser = authenticationService.changePasswordByUsername(findUser.get().getUsername(), request);
        log.info(updateUser.getPassword());
        log.info(findUser.get().getUsername());

//        boolean checkPasswords = passwordEncoder.matches(oldPassword, user.getPassword());
//
//        verify(userRepository).findByUsername(user.getUsername());
//        assertTrue(checkPasswords);
//        verify(passwordEncoder).encode(newPassword);
    }
}
