package com.example.transport_marketplace.authentication;

import com.example.transport_marketplace.enter.RefreshTokenRequest;
import com.example.transport_marketplace.enter.SignInRequest;
import com.example.transport_marketplace.enter.SignUpRequest;
import com.example.transport_marketplace.entity.tokens.RefreshToken;
import com.example.transport_marketplace.entity.tokens.RefreshTokenService;
import com.example.transport_marketplace.entity.users.Role;
import com.example.transport_marketplace.entity.users.User;
import com.example.transport_marketplace.entity.users.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public JwtAuthenticationResponse signUp(SignUpRequest request){

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
    }
    public JwtAuthenticationResponse signIn(SignInRequest request){

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken((User) user);
        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken.getToken())
                .build();
     }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateAccessToken(user);
                    return JwtAuthenticationResponse.builder()
                            .token(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }
    public JwtAuthenticationResponse deleteToken(RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(token ->{
                    User user = token.getUser();
                    refreshTokenService.deleteByUser(user);
                    String accessToken = jwtService.generateAccessToken(user);
                    return JwtAuthenticationResponse.builder()
                            .token(accessToken)
                            .refreshToken("")
                            .build();
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token:("));
    }
    @Transactional
    public void deleteByToken(String refreshToken){
        refreshTokenService.findByToken(refreshToken)
                .ifPresent(token -> {
                    refreshTokenService.deleteByUser(token.getUser());
                });
    }

}
