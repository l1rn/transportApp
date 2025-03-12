package com.example.transport_marketplace.test_controllers;

import com.example.transport_marketplace.controllers.RouteController;
import com.example.transport_marketplace.dto.routes.RouteRequest;
import com.example.transport_marketplace.exceptions.routes.Exceptions.BadRequestException;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.service.RouteService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteController routeController;

    @Test
    void getRoutes_ReturnsAllRoutes() throws IOException {
        List<Route> expectedRoutes = List.of(new Route(), new Route());
        when(routeService.getRoutes()).thenReturn(expectedRoutes);

        ResponseEntity<List<Route>> response = routeController.getRoutes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
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
    void getRouteById_InvalidId_ThrowsBadRequest() {
        assertThrows(BadRequestException.class, () -> {
            routeController.getRouteById("invalid_id");
        });
    }
    @Test
    void searchRoutes_WithFilters_ReturnsPaginatedResults() {
        // Given
        List<Route> mockRoutes = IntStream.range(0, 15)
                .mapToObj(i -> new Route())
                .collect(Collectors.toList());

        when(routeService.searchRoutes(any(), any(), any(), any()))
                .thenReturn(mockRoutes);

        // When
        ResponseEntity<?> response = routeController.searchRoutes(
                "Moscow", "Saint-Petersburg", "2024-03-15", "bus", 0, 10
        );

        // Then
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(15, body.get("totalElements"));
        assertEquals(2, body.get("totalPages"));
        assertEquals(0, body.get("currentPage"));
        assertEquals(10, ((List<?>) body.get("content")).size());
    }

    @Test
    void addRoute_InvalidRequest_ThrowsValidationException() {
        RouteRequest invalidRequest = new RouteRequest();
        BindingResult bindingResult = new BeanPropertyBindingResult(
                invalidRequest, "routeRequest");
        bindingResult.addError(new FieldError("routeRequest", "date", "required"));

        assertThrows(ValidationException.class, () -> {
            routeController.addRoute(invalidRequest, bindingResult);
        });
    }
}