package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.auth.SignUpRequest;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.repo.DeviceRepository;
import com.example.transport_marketplace.repo.RefreshTokenRepository;
import com.example.transport_marketplace.repo.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DeviceRepository deviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void signUp(SignUpRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Такой пользователь уже есть");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
    }

    @Transactional
    public JwtAuthenticationResponse signIn(SignInRequest request, HttpServletRequest httpServletRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String userAgent = httpServletRequest.getHeader("User-Agent");
        String ipAddress = httpServletRequest.getRemoteAddr();
        String deviceFingerprint = userAgent + ipAddress;

        Device newDevice = Device.builder()
                .deviceFingerprint(deviceFingerprint)
                .userAgent(userAgent)
                .user(user)
                .build();

        Device device = deviceRepository.findByDeviceFingerprintAndUser(deviceFingerprint, user)
                        .orElseGet(() -> deviceRepository.save(newDevice));

        user.getDevices().add(device);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenRepository.deleteByUserAndDevice(user, device);
        refreshTokenRepository.flush();

        Token refreshTokenEntity = Token.builder()
                .token(refreshToken)
                .user(user)
                .device(device)
                .expiryDate(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()))
                .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }


    public JwtAuthenticationResponse refreshToken(String refreshToken){
        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        Token storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenRepository.delete(storedToken);

        if (storedToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedToken);
            throw new RuntimeException("Refresh token expired");
        }

        User user = storedToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        Token newToken = Token.builder()
                .token(newRefreshToken)
                .user(user)
                .device(storedToken.getDevice())
                .expiryDate(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()))
                .build();
        refreshTokenRepository.save(newToken);

        return new JwtAuthenticationResponse(newAccessToken, newRefreshToken);
    }
    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }


}
