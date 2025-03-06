//package com.example.transport_marketplace.test_contorllers;
//
//import com.example.transport_marketplace.controllers.UsersController;
//import com.example.transport_marketplace.enums.Role;
//import com.example.transport_marketplace.model.User;
//import com.example.transport_marketplace.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.ComponentScan.Filter;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.times;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(controllers = UsersController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
//@ContextConfiguration(classes = { UsersController.class, UsersControllerTest.Config.class })
//@Import(UsersControllerTest.Config.class)
//public class UsersControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;
//
//    @TestConfiguration
//    static class Config {
//        @Bean
//        public UserService userService() {
//            return Mockito.mock(UserService.class);
//        }
//    }
//
//    @Test
//    void testGetAllUsers() throws Exception {
//        List<User> users = List.of(
//                new User(1, "user1", "pass1", Role.ROLE_USER),
//                new User(2, "user2", "pass2", Role.ROLE_ADMIN)
//        );
//        when(userService.getAllUsers()).thenReturn(users);
//
//        mockMvc.perform(get("/")
//                        .contentType("application/json"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(users.size()))
//                .andExpect(jsonPath("$[0].username").value("user1"))
//                .andExpect(jsonPath("$[1].username").value("user2"));
//
//        verify(userService, times(1)).getAllUsers();
//    }
//}
