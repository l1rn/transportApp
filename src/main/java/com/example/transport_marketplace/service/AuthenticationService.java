package com.example.transport_marketplace.service;

import com.example.transport_marketplace.enter.RefreshTokenRequest;
import com.example.transport_marketplace.enter.SignInRequest;
import com.example.transport_marketplace.enter.SignUpRequest;
import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.enter.JwtAuthenticationResponse;
import com.example.transport_marketplace.jwt.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
    private final TokenService tokenService;

    public JwtAuthenticationResponse signUp(SignUpRequest request){

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);
        var accessToken = jwtService.generateAccessToken(user);
        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }
    public JwtAuthenticationResponse signIn(SignInRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService.userDetailsService().loadUserByUsername(request.getUsername());
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = tokenService.createRefreshToken((User) user);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return JwtAuthenticationResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Transactional
    public JwtAuthenticationResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse response){
        Cookie[] cookies = httpServletRequest.getCookies();
        String refreshToken = null;
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("refreshToken")){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if(refreshToken == null){
            throw new RuntimeException("No refresh token found");
        }

        Token verifiedToken = tokenService.findByToken(refreshToken)
                .map(tokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        tokenService.deleteByUser(verifiedToken.getUser());
        String newAccessToken = jwtService.generateAccessToken(verifiedToken.getUser());
        Token newRefreshToken = tokenService.createRefreshToken(verifiedToken.getUser());
        String newRefreshTokenString = newRefreshToken.getToken();
        
        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshTokenString)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return JwtAuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
    @Transactional
    public ResponseEntity<Void> logout(HttpServletResponse response, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("refreshToken")){
                    String refreshToken = cookie.getValue();
                    tokenService.findByToken(refreshToken).ifPresent(
                            token -> {
                                tokenService.deleteByUser(token.getUser());
                            });
                    break;
                }
            }
        }
        ResponseCookie deleteCookie = ResponseCookie.from("refreshCookie", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());

        return ResponseEntity.noContent().build();
    }
}
