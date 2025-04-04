package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.dto.auth.SignInRequest;
import com.example.transport_marketplace.dto.auth.SignUpRequest;
import com.example.transport_marketplace.dto.jwt.JwtAuthenticationResponse;
import com.example.transport_marketplace.dto.users.ChangePasswordRequest;
import com.example.transport_marketplace.jwt.JwtService;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация")
@RequiredArgsConstructor
public class AuthController {
    private final TokenBlacklist tokenBlacklist;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshExpirationMs;
    @Value("${jwt.access-token-expiration}")
    private long accessExpirationMs;
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
        return ResponseEntity.ok("Registered");
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
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request,
                                    HttpServletRequest httpServletRequest,
                                    HttpServletResponse response) {
        JwtAuthenticationResponse authenticationResponse = authenticationService.signIn(request, httpServletRequest);

        response.setHeader("X-Token-Expires", String.valueOf(accessExpirationMs));

        setAuthCookies(response, authenticationResponse.getAccessToken(), authenticationResponse.getRefreshToken());
        return ResponseEntity.ok("Authorized");
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
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(HttpServletRequest request,
                                                                  HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Рефреш токена нету"));
        JwtAuthenticationResponse newTokens = authenticationService.refreshToken(refreshToken);
        response.addHeader("X-Token-Expires", String.valueOf(accessExpirationMs));

        setAuthCookies(response, newTokens.getAccessToken(), newTokens.getRefreshToken());
        return ResponseEntity.ok().build();
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
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try{
            Cookie[] cookies = request.getCookies();
            String accessToken = Arrays.stream(cookies)
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new RuntimeException("Акцес токена нету"));
            String refreshToken = Arrays.stream(cookies)
                    .filter(c -> "refreshToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new RuntimeException("Рефреш токена нету"));

            tokenBlacklist.revoke(accessToken);
            authenticationService.deleteRefreshToken(refreshToken);
            clearAuthCookies(response);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            return ResponseEntity.noContent().build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/session/delete/{id}")
    public ResponseEntity<?> deleteSessionByAgent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable int id,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String username = userDetails.getUsername();
            authenticationService.deleteSession(username, request, id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось удалить сессию");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/session/now")
    public ResponseEntity<?> checkNow(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request){
        try {
            String username = userDetails.getUsername();
            return ResponseEntity.ok(authenticationService.checkSession(username, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось получить сессию");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChangePasswordRequest changePasswordRequest){
        try {
            String username = userDetails.getUsername();
            return ResponseEntity.ok(authenticationService.changePasswordByUsername(username, changePasswordRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    private void setAuthCookies(HttpServletResponse response, String accessToken,
                                String refreshToken){
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(accessExpirationMs / 1000)
                .sameSite("Lax")
                .build();
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(refreshExpirationMs / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }

    private void clearAuthCookies(HttpServletResponse response){
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .maxAge(0)
                .path("/")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .maxAge(0)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
    }
}