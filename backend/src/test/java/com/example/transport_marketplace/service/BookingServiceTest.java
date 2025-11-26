package com.example.transport_marketplace.service;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.RouteRepository;
import com.example.transport_marketplace.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private BookingService bookingService;

    Booking testBooking;

    @BeforeEach
    public void setUp() {
        testBooking = TestFixtures.createTestBooking();

    }

    @Test
    void createBooking_shouldCreateBookingForUser(){
        Route route = testBooking.getRoute();
        route.setAvailableSeats(10);

        when(routeRepository.findById(1)).thenReturn(Optional.of(route));
        when(userRepository.findById(1)).thenReturn(Optional.of(testBooking.getUser()));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.createBooking(1, 1);

        assertNotNull(result);
        assertEquals(BookingStatus.PENDING, result.getStatus());
        assertEquals(1, result.getRoute().getId());
        assertEquals(testBooking.getUser(), result.getUser());

        verify(routeRepository).findById(1);
        verify(routeRepository).saveAndFlush(route);
        verify(userRepository).findById(1);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void getBooking_shouldReturnBooking_whenExists(){
        Booking booking = Booking.builder()
                .id(1)
                .build();

        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingById(1);

        assertNotNull(result);

        assertEquals(booking.getId(), result.getId());
        verify(bookingRepository).findById(1);
    }

    @Test
    void getBooking_shouldThrowException_whenNotFound(){
        when(bookingRepository.findById(123)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBookingById(123));

        verify(bookingRepository).findById(123);
    }

    @AfterEach
    public void tearDown(){
        reset(bookingRepository);
    }
}
