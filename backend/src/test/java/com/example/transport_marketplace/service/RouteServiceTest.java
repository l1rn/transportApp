package com.example.transport_marketplace.service;

import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoutes() {
        List<Route> routes = Arrays.asList(
                new Route(1, "Moscow", "Saint Petersburg", "2025-03-10", "Train", "10:00", "14:00", 50, 1000.50),
                new Route(2, "Moscow", "Kazan", "2025-03-12", "Bus", "08:00", "16:00", 40, 750.75)
        );
        when(routeRepository.findAll()).thenReturn(routes);

        List<Route> result = routeService.getRoutes();

        assertEquals(2, result.size());
        verify(routeRepository, times(1)).findAll();
    }

    @Test
    void testGetRouteById() {
        Route route = new Route(1, "Moscow", "Saint Petersburg", "2025-03-10", "Train", "10:00", "14:00", 50, 1000.50);
        when(routeRepository.findById(1)).thenReturn(Optional.of(route));

        Optional<Route> result = routeService.getRouteById(1);

        assertTrue(result.isPresent());
        assertEquals("Moscow", result.get().getRouteFrom());
        verify(routeRepository, times(1)).findById(1);
    }

    @Test
    void testAddRoute() {
        Route route = new Route(1, "Moscow", "Saint Petersburg", "2025-03-10", "Train", "10:00", "14:00", 50, 1000.50);
        when(routeRepository.save(route)).thenReturn(route);

        Route result = routeService.addRoute(route);

        assertNotNull(result);
        assertEquals("Moscow", result.getRouteFrom());
        verify(routeRepository, times(1)).save(route);
    }

    @Test
    void testDeleteRoute() {
        when(routeRepository.existsById(1)).thenReturn(true);
        doNothing().when(routeRepository).deleteById(1);

        boolean result = routeService.deleteRoute(1);

        assertTrue(result);
        verify(routeRepository, times(1)).existsById(1);
        verify(routeRepository, times(1)).deleteById(1);
    }

    @Test
    void testUpdateRoute() {
        Route existingRoute = new Route(1, "Moscow", "Saint Petersburg", "2025-03-10", "Train", "10:00", "14:00", 50, 1000.50);
        Route updatedRoute = new Route(1, "Moscow", "Kazan", "2025-03-12", "Bus", "08:00", "16:00", 40, 750.75);

        when(routeRepository.findById(1)).thenReturn(Optional.of(existingRoute));
        when(routeRepository.save(any(Route.class))).thenReturn(updatedRoute);

        Route result = routeService.updateRoute(1, updatedRoute);

        assertNotNull(result);
        assertEquals("Kazan", result.getRouteTo());
        assertEquals("2025-03-12", result.getDate());
        assertEquals("Bus", result.getTransport());
        verify(routeRepository, times(1)).findById(1);
        verify(routeRepository, times(1)).save(any(Route.class));
    }

    @Test
    void testSearchRoutes() {
        List<Route> routes = Arrays.asList(
                new Route(1, "Moscow", "Saint Petersburg", "2025-03-10", "Train", "10:00", "14:00", 50, 1000.50),
                new Route(2, "Moscow", "Kazan", "2025-03-12", "Bus", "08:00", "16:00", 40, 750.75)
        );
        when(routeRepository.searchRoutes("Moscow", null, null, null)).thenReturn(routes);

        List<Route> result = routeService.searchRoutes("Moscow", null, null, null);

        assertEquals(2, result.size());
        verify(routeRepository, times(1)).searchRoutes("Moscow", null, null, null);
    }
}
