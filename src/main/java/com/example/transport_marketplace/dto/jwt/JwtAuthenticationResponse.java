package com.example.transport_marketplace.dto.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Токен доступа")
public class JwtAuthenticationResponse {
    @Schema(description = "accessToken", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6Ikpva...")
    private String accessToken;

    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiIs...")
    private String refreshToken;
}
