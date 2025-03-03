package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.TokenBlacklist;
import com.example.transport_marketplace.enter.RefreshTokenRequest;
import com.example.transport_marketplace.enter.SignInRequest;
import com.example.transport_marketplace.enter.SignUpRequest;
import com.example.transport_marketplace.service.TokenService;

import com.example.transport_marketplace.enter.JwtAuthenticationResponse;
import com.example.transport_marketplace.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    @Autowired
    private final TokenService tokenService;
    private final TokenBlacklist tokenBlacklist;
    private final AuthenticationService authenticationService;
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request){
        return authenticationService.signUp(request);
    }
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request,
                                            HttpServletResponse response) {
        return authenticationService.signIn(request, response);
    }


    @Operation(summary = "refresh tokens")
    @PostMapping("/refresh")
    public JwtAuthenticationResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request, HttpServletResponse response){
        return authenticationService.refreshToken(request, response);
    }

    @Operation(summary = "Выход из системы")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();
        String accessToken = request.getAccessToken();

        tokenBlacklist.revoke(accessToken);
        tokenBlacklist.revoke(refreshToken);
        authenticationService.deleteTokenByUser(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

}
