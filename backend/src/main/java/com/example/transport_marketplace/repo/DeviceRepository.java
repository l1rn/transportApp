package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findByDeviceFingerprintAndUser(String deviceFingerPrint, User user);
    Optional<Device> findFirstByDeviceFingerprintAndUser(String deviceFingerPrint, User user);
    Optional<List<Device>> findByUser(User user);
    Optional<Device> findByUserAndUserAgent(User user, String userAgent);
    Optional<Object> findByUserAndUserAgent(Optional<User> user, String userAgent);

    void deleteByUser(User user);
    @Modifying
    @Query("DELETE FROM Device d WHERE d.user = :user AND d = :device")
    void deleteDeviceForUser(@Param("user") User user, @Param("device") Device device);
}
