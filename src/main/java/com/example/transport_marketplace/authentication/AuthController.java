package com.example.transport_marketplace.authentication;
import com.example.transport_marketplace.JwtAuthenticationResponse;
import com.example.transport_marketplace.JwtService;
import com.example.transport_marketplace.enter.SignInRequest;
import com.example.transport_marketplace.enter.SignUpRequest;
import com.example.transport_marketplace.entity.users.User;
import com.example.transport_marketplace.entity.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
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
    @Operation(summary = "Профиль")
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        User user = userService.getByUsername(username);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

}
