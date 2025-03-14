package com.example.transport_marketplace.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentRoleResponse {
    @NotBlank
    private String role;

}
