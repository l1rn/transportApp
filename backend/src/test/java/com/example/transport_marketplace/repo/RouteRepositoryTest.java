package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.Route;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RouteRepositoryTest {

    @Mock
    private RouteRepository routeRepository;

    private Route testRoute;

    @BeforeEach
    public void setUp() {
        testRoute = TestFixtures.createTestRoute();
    }

    @Test
    public void testSaveRoute() {
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);
        Route savedRoute = routeRepository.save(testRoute);

        assertNotNull(savedRoute);
        assertEquals("Moscow", savedRoute.getRouteFrom());

        verify(routeRepository).save(testRoute);
    }

    @Test
    public void testUpdateRoute() {
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);
        when(routeRepository.findById(testRoute.getId())).thenReturn(Optional.of(testRoute));

        assertNotNull(testRoute);
        assertEquals("Moscow", testRoute.getRouteFrom());
        testRoute.setRouteFrom("Yekaterinburg");
        routeRepository.save(testRoute);

        Route foundRoute = routeRepository.findById(testRoute.getId())
                .orElse(null);

        assertNotNull(foundRoute);
        assertEquals("Yekaterinburg", foundRoute.getRouteFrom());

        verify(routeRepository).save(testRoute);
        verify(routeRepository).findById(testRoute.getId());
    }

    @Test
    public void givenBook_whenSaved_thenCanBeFoundById() {
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);
        when(routeRepository.findById(testRoute.getId())).thenReturn(Optional.of(testRoute));

        Route savedRoute = routeRepository.save(testRoute);
        Route foundRoute = routeRepository.findById(testRoute.getId())
                .orElse(null);

        assertNotNull(foundRoute);

        assertEquals(foundRoute.getRouteFrom(), testRoute.getRouteFrom());
        assertEquals(foundRoute.getRouteTo(), testRoute.getRouteTo());
        assertEquals(foundRoute.getArrivalTime(), testRoute.getArrivalTime());

        verify(routeRepository).save(testRoute);
        verify(routeRepository).findById(testRoute.getId());
    }

    @Test
    public void testDeleteRoute() {
        doNothing().when(routeRepository).deleteById(testRoute.getId());
        when(routeRepository.save(any(Route.class))).thenReturn(testRoute);

        routeRepository.deleteById(testRoute.getId());
        routeRepository.save(testRoute);
        assertFalse(routeRepository.findById(testRoute.getId()).isPresent());

        verify(routeRepository).deleteById(testRoute.getId());
        verify(routeRepository).save(testRoute);
    }

    @AfterEach
    public void tearDown(){
        reset(routeRepository);
    }
}
