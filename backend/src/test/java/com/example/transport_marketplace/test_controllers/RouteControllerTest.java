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


import java.time.LocalDateTime;
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
                Route.builder()
                        .id(1)
                        .routeFrom("Челябинск")
                        .routeTo("Омск")
                        .transport("Авиа")
                        .destinationTime(LocalDateTime.of(2026, 12, 27, 8, 0))
                        .arrivalTime(LocalDateTime.of(2026, 12, 27,12, 30))
                        .availableSeats(55)
                        .price(3100)
                        .build(),
                Route.builder()
                        .id(1)
                        .routeFrom("Москва")
                        .routeTo("Санкт-Петербург")
                        .transport("Поезд")
                        .destinationTime(LocalDateTime.of(2026, 7, 15, 10, 30))
                        .arrivalTime(LocalDateTime.of(2026, 7, 15,14, 45))
                        .availableSeats(120)
                        .price(2500)
                        .build()
        );
    }

    @Test
    void getRoutes_ReturnsAllRoutes() {
        when(routeService.getRoutes()).thenReturn(mockRoutes);

        ResponseEntity<List<Route>> response = routeController.getRoutes();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Челябинск", response.getBody().get(0).getRouteFrom());

        verify(routeService, times(1)).getRoutes();
        verifyNoMoreInteractions(routeService);
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
}