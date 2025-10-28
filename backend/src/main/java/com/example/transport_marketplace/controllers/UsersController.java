package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.users.ChangeEmailRequest;
import com.example.transport_marketplace.dto.users.ConfirmCodeResponse;
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
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
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
    public ResponseEntity<?> getMyDevices(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        try {
            return ResponseEntity.ok(userService.getInfoByUsername(userDetails.getUsername()));
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Не удалось получать информацию о вас");
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/set-email")
    public ResponseEntity<?> requestUserEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ChangeEmailRequest emailDTO){
        try{
            return ResponseEntity.ok(userService.setUserEmail(userDetails.getUsername(), emailDTO.getEmail()));
        }
        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Null pointer error: " + e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/confirm-email")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> confirmUserEmail(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ConfirmCodeResponse codeResponse){
        try{
            return ResponseEntity.ok(userService.confirmEmail(userDetails.getUsername(), codeResponse.getCode()));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/account/top-up")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> requestWithdraw (
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam double amount
    ) {
        try{
            return ResponseEntity.ok(userService.requestTopUp(userDetails.getUsername(), amount));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось отправить запрос: " + e.getMessage());
        }
    }

    @PostMapping("/account/confirm-top-up")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> confirmWithdraw(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ConfirmCodeResponse codeResponse
    ) {
        try{
            return ResponseEntity.ok(userService.confirmTopUp(userDetails.getUsername(), codeResponse.getCode()));
        }
        catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Null pointer error: " + e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
