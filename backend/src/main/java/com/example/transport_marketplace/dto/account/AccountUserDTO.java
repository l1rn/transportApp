package com.example.transport_marketplace.dto.account;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountUserDTO {
    private String username;
    private double balance;
}
