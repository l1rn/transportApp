package com.example.transport_marketplace.service;


import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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
                Route.builder()
                        .id(1)
                        .routeFrom("Moscow")
                        .routeTo("Saint Petersburg")
                        .transport("Train")
                        .destinationTime(LocalDateTime.of(2026, 10, 24, 20, 20))
                        .arrivalTime(LocalDateTime.of(2026, 10, 24, 22, 20))
                        .availableSeats(50)
                        .price(1000)
                        .build(),
                Route.builder()
                        .id(1)
                        .routeFrom("Moscow")
                        .routeTo("Kazan")
                        .transport("Train")
                        .destinationTime(LocalDateTime.of(2026, 10, 24, 20, 20))
                        .arrivalTime(LocalDateTime.of(2026, 10, 24, 22, 20))
                        .availableSeats(50)
                        .price(1000)
                        .build()
        );
        when(routeRepository.findAll()).thenReturn(routes);

        List<Route> result = routeService.getRoutes();

        assertEquals(2, result.size());
        verify(routeRepository, times(1)).findAll();
    }

    @Test
    void testGetRouteById() {
        Route route = Route.builder()
                .id(1)
                .routeFrom("Moscow")
                .routeTo("Saint Petersburg")
                .transport("Train")
                .destinationTime(LocalDateTime.of(2026, 10, 24, 20, 20))
                .arrivalTime(LocalDateTime.of(2026, 10, 24, 22, 20))
                .availableSeats(50)
                .price(1000)
                .build();
        when(routeRepository.findById(1)).thenReturn(Optional.of(route));

        Optional<Route> result = routeService.getRouteById(1);

        assertTrue(result.isPresent());
        assertEquals("Moscow", result.get().getRouteFrom());
        verify(routeRepository, times(1)).findById(1);
    }

    // new
//    @Test
//    void testAddRoute() {
//
//        Route mockRoute = Route.builder()
//                .routeFrom("Moscow")
//                .routeTo("Saint Petersburg")
//                .date("2025-03-10")
//                .time("2025-03-10 10:00")
//                .arrivalTime("2025-03-10 14:00")
//                .transport("Поезд")
//                .availableSeats(50)
//                .price(1000.50)
//                .build();
//        mockRoute.setId(1);
//
//        when(routeRepository.save(any(Route.class))).thenReturn(mockRoute);
//
//        Route result = routeService.addRoute(mockRoute);
//
//        assertNotNull(result);
//        assertEquals(1, result.getId());
//        assertEquals("Moscow", result.getRouteFrom());
//        assertEquals("Saint Petersburg", result.getRouteTo());
//        assertEquals("2025-03-10", result.getDate());
//        assertEquals("2025-03-10 10:00", result.getTime());
//        assertEquals("2025-03-10 14:00", result.getArrivalTime());
//        assertEquals("Поезд", result.getTransport());
//        assertEquals(50, result.getAvailableSeats());
//        assertEquals(1000.50, result.getPrice(), 0.001);
//        verify(routeRepository, times(1)).save(any(Route.class));
//    }

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


//    @Test
//    void testUpdateRoute() {
//        Route existingRoute = new Route(1, "Moscow", "Saint Petersburg", "2025-03-10", "Train", "10:00", "14:00", 50, 1000.50);
//        Route updatedRoute = new Route(1, "Moscow", "Kazan", "2025-03-12", "Bus", "08:00", "16:00", 40, 750.75);
//
//        when(routeRepository.findById(1)).thenReturn(Optional.of(existingRoute));
//        when(routeRepository.save(any(Route.class))).thenReturn(updatedRoute);
//
//        Route result = routeService.updateRoute(1, updatedRoute);
//
//        assertNotNull(result);
//        assertEquals("Kazan", result.getRouteTo());
//        assertEquals("2025-03-12", result.getDate());
//        assertEquals("Bus", result.getTransport());
//        verify(routeRepository, times(1)).findById(1);
//        verify(routeRepository, times(1)).save(any(Route.class));
//    }

    @Test
    void testSearchRoutes() {
        List<Route> routes = Arrays.asList(
                Route.builder()
                        .id(1)
                        .routeFrom("Moscow")
                        .routeTo("Saint Petersburg")
                        .transport("Train")
                        .destinationTime(LocalDateTime.of(2026, 10, 24, 20, 20))
                        .arrivalTime(LocalDateTime.of(2026, 10, 24, 22, 20))
                        .availableSeats(50)
                        .price(1000)
                        .build(),
                Route.builder()
                        .id(1)
                        .routeFrom("Moscow")
                        .routeTo("Kazan")
                        .transport("Train")
                        .destinationTime(LocalDateTime.of(2026, 10, 24, 20, 20))
                        .arrivalTime(LocalDateTime.of(2026, 10, 24, 22, 20))
                        .availableSeats(50)
                        .price(1000)
                        .build()
        );
        when(routeRepository.searchRoutes("Moscow", null,  null, null, null)).thenReturn(routes);

        List<Route> result = routeService.searchRoutes("Moscow", null, null, null, null);

        assertEquals(2, result.size());
        verify(routeRepository, times(1)).searchRoutes("Moscow", null, null, null, null);
    }
}
