package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.RouteController;
import com.example.transport_marketplace.security.JwtAuthenticationFilter;
import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.jwt.TokenBlacklist;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.service.RouteService;
import com.example.transport_marketplace.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RouteControllerWithEndpointsTest {
    ObjectMapper objectMapper = new ObjectMapper();

    MockMvc mockMvc;

    List<Route> mockRoutes;

    @Mock
    private RouteService routeService;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklist tokenBlacklist;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private RouteController routeController;

    int sizeof;
    @BeforeEach
    void setupRoutes() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(routeController)
                .addFilter(new JwtAuthenticationFilter(jwtService, userDetailsService, tokenBlacklist))
                .defaultRequest(get("/").with(csrf()).characterEncoding(StandardCharsets.UTF_8))
                .build();
        mockRoutes = List.of(
                new Route(1,"Челябинск", "Омск", "2026-12-27", "Авиа",
                        "2026-12-27 08:00:00", "2026-12-27 12:30:00", 55, 3100),
                new Route(2, "Москва", "Санкт-Петербург", "2026-07-15", "Поезд",
                        "2026-07-15 10:30:00", "2026-07-15 14:45:00", 120, 2500),
                new Route(3, "Новосибирск", "Красноярск", "2026-08-01", "Авиа",
                        "2026-08-01 07:45:00", "2026-08-01 10:15:00", 40, 4200),
                new Route(5,"Челябинск", "Омск", "2026-12-24", "Поезд",
                        "2026-12-27 08:00:00", "2026-12-27 12:30:00", 100, 3200)
        );
        sizeof = mockRoutes.size();
    }
    @Test
    @Order(1)
    void getAllRoutesWithMockMvc_ShouldReturn200() throws Exception {
        when(routeService.getRoutes()).thenReturn(mockRoutes);

        mockMvc.perform(get("/api/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(sizeof)))
                .andExpect(jsonPath("$[0].routeFrom").value("Челябинск"))
                .andExpect(jsonPath("$[1].routeFrom").value("Москва"))
                .andExpect(jsonPath("$[2].routeFrom").value("Новосибирск"))
                .andExpect(jsonPath("$[0].price").value(3100))
                .andExpect(jsonPath("$[1].transport").value("Поезд"))
                .andExpect(jsonPath("$[2].arrivalTime").value("2026-08-01 10:15:00"));
    }
    @Test
    @Order(1)
    void getRouteById() throws Exception{
        int id = mockRoutes.getFirst().getId();
        when(routeService.getRouteById(mockRoutes.getFirst().getId())).thenReturn(Optional.ofNullable(mockRoutes.getFirst()));

        mockMvc.perform(get("/api/routes/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    void searchRoutes_WithDifferentFilters_ShouldReturnFilteredRoutes() throws Exception{
        when(routeService.searchRoutes(null, null, null, null, null, null))
                .thenReturn(mockRoutes);

        mockMvc.perform(get("/api/routes/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(sizeof)))
                .andExpect(jsonPath("$.totalElements").value(sizeof));

        List<Route> filteredByFrom = mockRoutes.stream()
                .filter(r -> r.getRouteFrom().equals("Челябинск"))
                .toList();

        when(routeService.searchRoutes("Челябинск", null, null, null, null, null))
                .thenReturn(filteredByFrom);

        mockMvc.perform(get("/api/routes/search")
                .param("routeFrom", "Челябинск")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].routeFrom").value("Челябинск"))
                .andExpect(jsonPath("$.content[1].availableSeats").value(100));
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "ADMIN")
    void createRoute_ShouldReturn201() throws Exception{
        Route route = Route.builder()
                .id(4)
                .routeFrom("Казань")
                .routeTo("Екатеринбург")
                .date("2025-12-12")
                .transport("Авиа")
                .time("2025-12-12 7:00:00")
                .arrivalTime("2025-12-12 11:00:00")
                .availableSeats(100)
                .price(4000)
                .build();

        when(routeService.addRoute(any(Route.class))).thenReturn(route);

        mockMvc.perform(post("/api/routes/panel/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(route)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.routeFrom").value("Казань"))
                .andExpect(jsonPath("$.routeTo").value("Екатеринбург"))
                .andExpect(jsonPath("$.price").value(4000));
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "ADMIN")
    void updateRoute_ShouldReturn200() throws Exception{
        int id = mockRoutes.get(1).getId();

        Route newRoute = Route.builder()
                .routeFrom("Новый маршрут")
                .routeTo("123")
                .date("2022-12-22")
                .transport("Поезд")
                .time("2022-12-22 12:12:12")
                .arrivalTime("2022-12-22 16:00:00")
                .availableSeats(152)
                .price(7777)
                .build();

        when(routeService.updateRoute(eq(id), any(Route.class))).thenReturn(newRoute);

        mockMvc.perform(put("/api/routes/panel/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newRoute)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.routeTo").value("123"))
                .andExpect(jsonPath("$.price").value(7777));
    }

    @Test
    @Order(4)
    @WithMockUser(roles = "ADMIN")
    void deleteRoute_ShouldReturn204() throws Exception{
        int id = mockRoutes.getLast().getId();
        when(routeService.deleteRoute(eq(id))).thenReturn(true);

        mockMvc.perform(delete("/api/routes/panel/delete/{id}", id))
                .andExpect(status().isNoContent());
    }
    @Test
    @Order(5)
    @WithMockUser(roles = "ADMIN")
    void deleteRoute_ShouldReturn404() throws Exception{
        int id = mockRoutes.getLast().getId();
        when(routeService.deleteRoute(eq(id))).thenReturn(false);

        mockMvc.perform(delete("/api/routes/panel/delete/{id}", id))
                .andExpect(status().isNotFound());
    }
}
