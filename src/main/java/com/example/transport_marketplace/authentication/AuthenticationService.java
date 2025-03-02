package com.example.transport_marketplace.authentication;

import com.example.transport_marketplace.enter.RefreshTokenRequest;
import com.example.transport_marketplace.enter.SignInRequest;
import com.example.transport_marketplace.enter.SignUpRequest;
import com.example.transport_marketplace.entity.tokens.RefreshToken;
import com.example.transport_marketplace.entity.tokens.RefreshTokenService;
import com.example.transport_marketplace.entity.users.Role;
import com.example.transport_marketplace.entity.users.User;
import com.example.transport_marketplace.entity.users.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest request, HttpServletResponse response){
        String refreshToken = request.getRefreshToken();
        RefreshToken verifiedToken = refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        String newAccessToken = jwtService.generateAccessToken(verifiedToken.getUser());
        String newRefreshToken = jwtService.generateRefreshToken(verifiedToken.getUser());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return JwtAuthenticationResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
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
    public void deleteTokenByUser(String refreshToken){
        refreshTokenService.findByToken(refreshToken)
                .ifPresent(token -> {
                    refreshTokenService.deleteByUser(token.getUser());
                });
    }

}
