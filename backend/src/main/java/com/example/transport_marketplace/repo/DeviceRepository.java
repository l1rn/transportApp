package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Token;
import com.example.transport_marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Optional<Device> findByDeviceFingerprintAndUser(String deviceFingerPrint, User user);
    List<Device> findByUser(User user);

    @Modifying
    @Query("DELETE FROM Device d WHERE d.user = :user AND d.refreshToken = :refreshToken")
    Device deleteDevice(@Param("user") User user, @Param("refreshToken")Token token);
}
