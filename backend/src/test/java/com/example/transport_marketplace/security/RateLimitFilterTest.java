package com.example.transport_marketplace.security;


import com.example.transport_marketplace.controllers.RouteController;
import com.example.transport_marketplace.enums.Role;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class RateLimitFilterTest {

    @InjectMocks
    private RouteController routeController;

    @Mock
    UserService userService;
    User user;
    User user1;
    @BeforeEach
    void setupUsers(){
        user = User.builder()
                .username("test1")
                .password("123")
                .role(Role.ROLE_USER)
                .build();
        user1 = User.builder()
                .username("test2")
                .password("111")
                .role(Role.ROLE_ADMIN)
                .build();
        userService.save(user);
        userService.save(user1);
    }

    @Test
    void loadAllRoutesByTwoUsers(){
        for(int i = 0; i < 210; i++){

        }
        for (int i = 0; i < 190; i++){

        }
    }

}
