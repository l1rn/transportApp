package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.auth.SignUpRequest;
import com.example.transport_marketplace.dto.users.ChangePasswordRequest;
import com.example.transport_marketplace.dto.users.sessions.UserDeviceNowResponse;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.repo.DeviceRepository;
import com.example.transport_marketplace.repo.RefreshTokenRepository;
import com.example.transport_marketplace.repo.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final DeviceRepository deviceRepository;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${hmac.key}")
    private String hmacSecretKey;

    public void signUp(SignUpRequest request){

        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Used");
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
        String deviceFingerprint = "agent:" + userAgent + "!ip:" + ipAddress;

        String mac = hmacSha256Base64(hmacSecretKey, deviceFingerprint);
        Device newDevice = Device.builder()
                .deviceFingerprint(mac)
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

    public void changePasswordByUsername(String username, ChangePasswordRequest request){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        if(!passwordEncoder.matches(oldPassword, user.getPassword())){
            throw new RuntimeException("Старый пароль неправильный!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }

    public UserDeviceNowResponse checkSession(String username, HttpServletRequest request){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        Device device = deviceRepository.findByUserAndUserAgent(user, request.getHeader("User-Agent"))
                .orElseThrow(() -> new RuntimeException("Устройство не найдено"));

        int deviceId = device.getId();

        return new UserDeviceNowResponse(deviceId);
    }

    public boolean checkForAuth(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        String accessToken = Arrays.stream(cookies)
                .filter(c -> "accessToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Токена нет"));

        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            throw new RuntimeException("Нету устройства");
        }

        List<Integer> devices = jwtService.getDevicesFromToken(accessToken);

        String username = jwtService.getUsernameFromToken(accessToken);
        Optional<User> user = userRepository.findByUsername(username);

        Device device = (Device) deviceRepository.findByUserAndUserAgent(user, userAgent)
                        .orElseThrow(() -> new RuntimeException("Устройство не найден"));

        log.info(String.valueOf(device.getId()));
        log.info(devices.toString());

        if(!devices.contains(device.getId())){
            throw new RuntimeException("Устройства больше нет в сессии");
        }
        return jwtService.validateToken(accessToken);
    }

    @Transactional
    public void deleteSession(String username, int deviceId){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        refreshTokenRepository.deleteByUserAndDevice(user, deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Устройство не найдено")));

        deviceRepository.deleteById(deviceId);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(refreshTokenRepository::delete);
    }

    public static String hmacSha256Base64(String secretKey, String message){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
