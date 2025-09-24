package com.example.transport_marketplace.dto.users;

import com.example.transport_marketplace.model.Device;
import lombok.Data;

import java.util.List;

@Data
public class UserSettingsResponse {
    private int id;
    private String username;
    private List<Device> devices;
}
