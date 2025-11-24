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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @AfterEach
    public void tearDown(){
        reset(routeRepository);
    }
}
