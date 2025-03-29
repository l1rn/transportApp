package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.routes.RouteDTO;
import com.example.transport_marketplace.dto.routes.RouteRequest;
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

    // new
    @Test
    void testAddRoute() {
        RouteRequest request = new RouteRequest(
                "Moscow",
                "Saint Petersburg",
                "2025-03-10",
                "2025-03-10 10:00",
                "2025-03-10 14:00",
                "Train",
                50,
                1000.50
        );

        Route mockRoute = Route.builder()
                .routeFrom(request.getRouteFrom())
                .routeTo(request.getRouteTo())
                .date(request.getDate())
                .time(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .transport(request.getTransport())
                .availableSeats(request.getAvailableSeats())
                .price(request.getPrice())
                .build();
        mockRoute.setId(1);

        when(routeRepository.save(any(Route.class))).thenReturn(mockRoute);

        RouteDTO result = routeService.addRoute(request);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Moscow", result.getRouteFrom());
        assertEquals("Saint Petersburg", result.getRouteTo());
        assertEquals("2025-03-10", result.getDate());
        assertEquals("2025-03-10 10:00", result.getDepartureTime());
        assertEquals("2025-03-10 14:00", result.getArrivalTime());
        assertEquals("Train", result.getTransport());
        assertEquals(50, result.getAvailableSeats());
        assertEquals(1000.50, result.getPrice(), 0.001);
        verify(routeRepository, times(1)).save(any(Route.class));
    }

    // new
    @Test
    void testDeleteRouteWhenExists() {
        Route mockRoute = new Route();
        when(routeRepository.findById(1)).thenReturn(Optional.of(mockRoute));
        doNothing().when(routeRepository).deleteById(1);

        boolean result = routeService.deleteRoute(1);

        assertTrue(result);
        verify(routeRepository).findById(1);
        verify(routeRepository).deleteById(1);
    }

    // new
    @Test
    void testDeleteRouteWhenNotExists() {

        when(routeRepository.findById(1)).thenReturn(Optional.empty());

        boolean result = routeService.deleteRoute(1);

        assertFalse(result);
        verify(routeRepository).findById(1);
        verify(routeRepository, never()).deleteById(1);
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
