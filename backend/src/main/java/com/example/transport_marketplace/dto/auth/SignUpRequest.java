package com.example.transport_marketplace.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {
    @Schema(description = "Имя пользователя", example = "Alina")
    @Size(min = 3, max = 40, message = "Имя пользователя должно содержать от 3 до 40 символов")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Schema(description = "password", example = "superpassword$!@#")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;
}
