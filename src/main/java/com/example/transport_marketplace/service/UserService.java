package com.example.transport_marketplace.service;
import com.example.transport_marketplace.dto.auth.CurrentRoleResponse;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final JwtService jwtService;

    public User save(User user){
        return repository.save(user);
    }

    public User create(User user){
        if(repository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Имя пользователя занято!");
        }
        return repository.save(user);
    }

    public List<User> getAllUsers(){
        return repository.findAll();
    }

    @Transactional
    public Optional<Role> getUserRole(String username) {
        return repository.findByUsername(username)
                .map(User::getRole);
    }

    public User getById(int id){
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь c ID" + id + " не найден"));
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
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

    public User setAdmin(User user){
        user.setRole(Role.ROLE_ADMIN);
        return repository.save(user);
    }
    public User setUser(User user){
        user.setRole(Role.ROLE_USER);
        return repository.save(user);
    }
}
