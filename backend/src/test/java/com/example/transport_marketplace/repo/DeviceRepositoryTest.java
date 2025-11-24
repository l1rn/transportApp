package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Route;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeviceRepositoryTest {

    @Mock
    private DeviceRepository deviceRepository;

    private Device deviceTest;

    @BeforeEach
    public void setUp() {
        deviceTest = TestFixtures.createTestDevice();
    }

    @Test
    public void givenBook_whenSaved_thenCanBeFoundById() {
        when(deviceRepository.save(any(Device.class))).thenReturn(deviceTest);
        when(deviceRepository.findById(deviceTest.getId())).thenReturn(Optional.of(deviceTest));

        Device savedDevice = deviceRepository.save(deviceTest);
        Device foundDevice = deviceRepository.findById(deviceTest.getId())
                .orElse(null);

        assertNotNull(foundDevice);

        assertEquals(foundDevice.getDeviceFingerprint(), deviceTest.getDeviceFingerprint());
        assertEquals(foundDevice.getUserAgent(), deviceTest.getUserAgent());

        verify(deviceRepository).save(deviceTest);
        verify(deviceRepository).findById(deviceTest.getId());
    }

    @AfterEach
    public void tearDown(){
        reset(deviceRepository);
    }
}
