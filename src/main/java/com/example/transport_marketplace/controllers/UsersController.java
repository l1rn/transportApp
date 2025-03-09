package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.auth.CurrentRoleResponse;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.UserRepository;
import com.example.transport_marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Операции относящие к пользователям")
public class UsersController {
    private final UserService userService;
    private final JwtService jwtService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @Operation(summary = "Получить всех юзеров", description = "Возращает всех юзеров")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/role")
    public ResponseEntity<Role> getRole(@RequestHeader("Authorization") String authHeader) {
        System.out.println(authHeader);
        String accessToken = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(accessToken);
        System.out.println(username);
        Optional<Role> role = userService.getUserRole(username);

        return role.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
