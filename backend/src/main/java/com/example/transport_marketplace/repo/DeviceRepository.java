package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByDeviceFingerprintAndUser(String deviceFingerPrint, User user);
    List<Device> findByUser(User user);
    Optional<Device> findByUserAndUserAgent(User user, String userAgent);

    Optional<Object> findByUserAndUserAgent(Optional<User> user, String userAgent);
}
