package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.auth.CurrentRoleResponse;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Операции относящие к пользователям")
public class UsersController {
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    @Operation(summary = "Получить всех юзеров", description = "Возращает всех юзеров")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me/role")
    @Operation(summary = "Получить роль текущего пользователя", description = "Возвращает роль аутентифицированного пользователя")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CurrentRoleResponse> getCurrentUserRole() {
        User currentUser = userService.getCurrentUser();
        CurrentRoleResponse response = new CurrentRoleResponse(currentUser.getRole().name());
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/{id}")
    public ResponseEntity<User> setAdminRole(@PathVariable int id) {
        User user = userService.getById(id);
        userService.setAdmin(user);
        return ResponseEntity.ok(user);
    }

}
