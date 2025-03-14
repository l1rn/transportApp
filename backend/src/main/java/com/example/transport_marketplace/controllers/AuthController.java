package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.dto.auth.LogoutRequest;
import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.auth.SignUpRequest;
import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.dto.jwt.RefreshTokenRequest;
import com.example.transport_marketplace.jwt.TokenBlacklist;

import com.example.transport_marketplace.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация")
@RequiredArgsConstructor
public class AuthController {
    private final TokenBlacklist tokenBlacklist;
    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Создаёт нового пользователя в системе. Требуется уникальный имя и пароль. Возвращает сообщение об успешной регистрации."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class),
                            examples = @ExampleObject(value = "\"Пользователь зарегистрирован\""))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или имя уже занят",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "\"Имя уже используется\"")))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) {
        authenticationService.signUp(request);
        return ResponseEntity.ok("Пользователь зарегистрирован");
    }
    @Operation(
            summary = "Вход пользователя",
            description = "Аутентифицирует пользователя и возвращает JWT-токены (access и refresh)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {

        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    @Operation(
            summary = "Обновление токенов",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Обновляет access-токен с помощью refresh-токена."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токены обновлены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtAuthenticationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Недействительный refresh-токен")
    })
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request.getRefreshToken()));
    }

    @Operation(
            summary = "Выход из системы",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Завершает сессию пользователя, добавляя access-токен в blacklist и удаляя refresh-токен."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Успешный выход"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
    })
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(
            @RequestBody LogoutRequest request,
            @RequestHeader(name = "Authorization") String authHeader
    ) {
        String accessToken = authHeader.substring(7);
        tokenBlacklist.revoke(accessToken);
        authenticationService.deleteTokenByUser(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

}
