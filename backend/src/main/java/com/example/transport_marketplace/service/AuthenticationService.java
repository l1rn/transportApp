    package com.example.transport_marketplace.service;

    import com.example.transport_marketplace.dto.auth.SignInRequest;
    import com.example.transport_marketplace.dto.auth.SignUpRequest;
    import com.example.transport_marketplace.model.Token;
    import com.example.transport_marketplace.enums.Role;
    import com.example.transport_marketplace.model.User;
    import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
    import com.example.transport_marketplace.jwt.JwtService;
    import com.example.transport_marketplace.repo.RefreshTokenRepository;
    import com.example.transport_marketplace.repo.UserRepository;
    import jakarta.transaction.Transactional;
    import lombok.AllArgsConstructor;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.Instant;

    @Service
    @AllArgsConstructor
    public class AuthenticationService {
        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
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
        public JwtAuthenticationResponse signIn(SignInRequest request) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            refreshTokenRepository.deleteByUser(user);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            Token refreshTokenEntity = Token.builder()
                    .token(refreshToken)
                    .user(user)
                    .expiryDate(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()))
                    .build();

            refreshTokenRepository.save(refreshTokenEntity);

            return new JwtAuthenticationResponse(accessToken, refreshToken);
        }


        public JwtAuthenticationResponse refreshToken(String refreshToken){
            if (!jwtService.validateToken(refreshToken)) {
                throw new RuntimeException("Invalid refresh token");
            }

            String username = jwtService.getUsernameFromToken(refreshToken);

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
                    .expiryDate(Instant.now().plusMillis(jwtService.getRefreshExpirationMs()))
                    .build();
            refreshTokenRepository.save(newToken);

            return new JwtAuthenticationResponse(newAccessToken, newRefreshToken);
        }
        @Transactional
        public void deleteTokenByUser(String refreshToken) {
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent(refreshTokenRepository::delete);
        }

    }
