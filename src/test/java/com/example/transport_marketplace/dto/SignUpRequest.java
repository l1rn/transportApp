package com.example.transport_marketplace.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
}
