package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.auth.CurrentRoleResponse;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(
            summary = "Получение списка всех пользователей",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Возвращает список всех пользователей. Доступно только администраторам."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class, type = "array"))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён (не администратор)")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
            summary = "Получение роли текущего пользователя",
            description = "Возвращает роль аутентифицированного пользователя (например, ROLE_USER или ROLE_ADMIN)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль пользователя",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrentRoleResponse.class))),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/role")
    public ResponseEntity<CurrentRoleResponse> getCurrentUserRole() {
        User currentUser = userService.getCurrentUser();
        CurrentRoleResponse response = new CurrentRoleResponse(currentUser.getRole().name());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Назначение роли администратора",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Делает пользователя администратором. Доступно только текущим администраторам."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль обновлена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён"),
            @ApiResponse(responseCode = "400", description = "Пользователь уже администратор")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/{id}")
    public ResponseEntity<User> setAdminRole(@PathVariable(name = "id") int id) {
        User user = userService.getById(id);
        if (userService.getUserRole(user) == Role.ROLE_ADMIN) {
            throw new RuntimeException("Вы уже являетесь админом");
        }
        userService.setAdmin(user);
        return ResponseEntity.ok(user);
    }
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id){
        User user = userService.getById(id);
        if(user == null){
            throw new RuntimeException("Такого пользователя нет");
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
