package com.example.transport_marketplace.entity;
import com.example.transport_marketplace.TransportMarketplaceApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User save(User user){
        return repository.save(user);
    }

    public User create(User user){
        if(repository.existsByUsername(user.getUsername())){
            throw new RuntimeException("Имя пользователя занято!");
        }
        return repository.save(user);
    }

    public User getByUsername(String username){
        Optional<User> optionalUser = repository.findByUsername(username);

        if(optionalUser.isPresent()){
            return optionalUser.get();
        }
        else{
            throw new UsernameNotFoundException("Такого пользователя нет!");
        }
    }

    public UserDetailsService userDetailsService(){
        return this::getByUsername;
    }

    public User getCurrentUser(){
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
    @Deprecated
    public void getAdmin(){
        var user = getCurrentUser();
        user.setRole(User.Role.ROLE_ADMIN);
        save(user);
    }
}
