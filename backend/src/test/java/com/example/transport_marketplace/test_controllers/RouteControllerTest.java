package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.RouteController;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.service.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteController routeController;

    private List<Route> mockRoutes;

    @BeforeEach
    void setUp(){
        mockRoutes = Arrays.asList(
                new Route(1,"Челябинск", "Омск", "2026-12-27", "2026-12-27 08:00:00",
                        "2026-12-27 12:30:00", "Авиа", 55, 3100),
                new Route(2, "Москва", "Санкт-Петербург", "2026-07-15", "2026-07-15 10:30:00",
                        "2026-07-15 14:45:00", "Поезд", 120, 2500)
        );
    }

    @Test
    void getRouteById_ValidId_ReturnsRoute() {
        int routeId = 1;
        Route expectedRoute = new Route();
        when(routeService.getRouteById(routeId)).thenReturn(Optional.of(expectedRoute));

        ResponseEntity<?> response = routeController.getRouteById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedRoute, response.getBody());
    }
    
    @Test
    void searchRoutes_WithFilters_ReturnsPaginatedResults() {
        List<Route> mockRoutes = IntStream.range(0, 15)
                .mapToObj(i -> new Route())
                .collect(Collectors.toList());

        when(routeService.searchRoutes(any(), any(), any(), any(), anyDouble(), anyDouble()))
                .thenReturn(mockRoutes);

        ResponseEntity<?> response = routeController.searchRoutes(
                "Moscow", "Saint-Petersburg", "2024-03-15", "bus",
                0.0, 10000.0, 0, 10
        );

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(15, body.get("totalElements"));
        assertEquals(2, body.get("totalPages"));
        assertEquals(0, body.get("currentPage"));
        assertEquals(10, ((List<?>) body.get("content")).size());
    }

}