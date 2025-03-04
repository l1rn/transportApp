package com.example.transport_marketplace.enter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Выход")
public class LogoutRequest {
    @Schema(description = "Refresh Token")
    @NotBlank
    private String refreshToken;
    @Schema(description = "Access Token")
    @NotBlank
    private String accessToken;
}
