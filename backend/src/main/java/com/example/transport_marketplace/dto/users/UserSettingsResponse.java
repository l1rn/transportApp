package com.example.transport_marketplace.dto.users;

import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Device;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSettingsResponse {
    private int id;
    private String username;
    private Role role;
    private List<Device> devices;
}
