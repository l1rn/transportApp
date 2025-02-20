package com.example.transport_marketplace.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserService {
    private static final String USERS_FILE_PATH = "src/main/resources/users.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    public UserService(){
        loadUsers();
    }

    private void loadUsers(){
        File file = new File(USERS_FILE_PATH);
        if(file.exists()){
            try {
                List<User> loadedUsers = objectMapper.readValue(file, new TypeReference<List<User>>() {});
                users.addAll(loadedUsers);
                nextId = users.stream().mapToInt(User::getId).max().orElse(0) + 1;
            } catch (IOException e) {
                System.err.println("Ошибка загрузки пользователей: " + e.getMessage());
            }
        }
    }
    private void saveUsers(){
        try {
            objectMapper.writeValue(new File(USERS_FILE_PATH), users);
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
    public synchronized Optional<User> findByUsername(String username) {
        return users.stream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findFirst();
    }
    public synchronized User registerUser(String username, String password) {
        if (findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь уже существует");
        }
        User user = new User(nextId++, username, password);
        users.add(user);
        saveUsers();
        return user;
    }
}
