package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.enter.*;
import com.example.transport_marketplace.jwt.TokenBlacklist;

import com.example.transport_marketplace.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final TokenBlacklist tokenBlacklist;
    private final AuthenticationService authenticationService;
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request){
        authenticationService.signUp(request);
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request.getRefreshToken()));
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequest request) {
        tokenBlacklist.revoke(request.getAccessToken());

        authenticationService.deleteTokenByUser(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }
}
