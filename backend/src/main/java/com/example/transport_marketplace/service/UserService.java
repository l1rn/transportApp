package com.example.transport_marketplace.service;
import com.example.transport_marketplace.dto.users.UserSettingsResponse;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Device;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository repository;
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private final BookingRepository bookingRepository;
    @Autowired
    private final RouteRepository routeRepository;
    @Autowired
    private final DeviceRepository deviceRepository;

    @CachePut(value = "users", key = "#id")
    public User save(User user){
        return repository.save(user);
    }

    @CacheEvict(value = "users")
    @Transactional
    public void delete(int id){
        User user = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь c ID " + id + " не найден"));
        refreshTokenRepository.deleteByUser(user);
        bookingRepository.deleteByUserId(id);

        List<Booking> bookings = bookingService.getBookingByUserId(id);
        bookings.forEach(booking -> {
            Route route = booking.getRoute();
            route.setAvailableSeats(route.getAvailableSeats() + 1);
            routeRepository.save(route);
        });
        repository.delete(user);
    }

    public User create(User user){
        if(repository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Имя пользователя занято!");
        }
        return save(user);
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    @Transactional
    public Role getUserRole(User user) {
        return user.getRole();
    }

    @Cacheable(value = "users", key = "#id")
    public User getById(int id){
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь c ID" + id + " не найден"));
    }

    @Cacheable(value = "users", key = "#username")
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserDetailsService userDetailsService(){
        return this::getByUsername;
    }

    public User getCurrentUser(){
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserSettingsResponse getSettingsByUsername(String username){
        User user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        List<Device> devices = deviceRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Не найдено ни одно устройсто для: " + username));

        UserSettingsResponse response = UserSettingsResponse.builder()
                .username(username)
                .role(user.getRole())
                .devices(devices)
                .build();
        return response;
    }

    public void setAdmin(User user){
        user.setRole(Role.ROLE_ADMIN);
        save(user);
    }
}
