package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "Операции относящие к пользователям")
public class UsersController {
    private final UserService userService;

    @Operation(
            summary = "Получение списка всех пользователей",
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
            summary = "Назначение роли администратора",
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

    @Operation(summary = "Удаление юзера")
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<?> getMyDevices(@AuthenticationPrincipal UserDetails userDetails){
        try {
            String username = userDetails.getUsername();
            return ResponseEntity.ok(userService.getSettingsByUsername(username));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Не удалось получать информацию о вас");
        }
    }

    @PostMapping("/account/top-up")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdraw(
            HttpServletRequest request,
            @RequestBody String amount
    ){
        try{
            Cookie[] cookies = request.getCookies();
            String accessToken = Arrays.stream(cookies)
                    .filter(c -> "accessToken".equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new RuntimeException("Нету куков"));
            double amountD = Double.parseDouble(amount);
            return ResponseEntity.ok(userService.requestTopUp(accessToken, amountD));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось пополнить");
        }
    }
}
