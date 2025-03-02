package com.example.transport_marketplace.authentication;
import com.example.transport_marketplace.enter.RefreshTokenRequest;
import com.example.transport_marketplace.enter.SignInRequest;
import com.example.transport_marketplace.enter.SignUpRequest;
import com.example.transport_marketplace.entity.tokens.RefreshTokenService;

import com.example.transport_marketplace.entity.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    @Autowired
    RefreshTokenService refreshTokenService;
//    private final TokenBlacklist tokenBlacklist;
    private final AuthenticationService authenticationService;
    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request){
        return authenticationService.signUp(request);
    }
    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request){

        return authenticationService.signIn(request);
    }

    @Operation(summary = "refresh tokens")
    @PostMapping("/refresh")
    public JwtAuthenticationResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request, HttpServletResponse response){
        return authenticationService.refreshToken(request, response);
    }

    @Operation(summary = "Выход из системы")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshTokenRequest request){

        authenticationService.deleteTokenByUser(request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }

}
