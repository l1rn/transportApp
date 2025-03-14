package com.example.transport_marketplace.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Обновление токена")
public class RefreshTokenRequest {
    @Schema(description = "Refresh Token")
    @NotBlank
    private String refreshToken;
}
