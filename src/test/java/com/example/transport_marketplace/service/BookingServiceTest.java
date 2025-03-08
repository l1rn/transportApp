package com.example.transport_marketplace.service;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.repo.BookingRepository;
import com.example.transport_marketplace.repo.RouteRepository;
import com.example.transport_marketplace.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Route route;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = new User(1, "user1", "password", null);
        route = new Route();
        route.setId(1);
        route.setAvailableSeats(10);
        booking = Booking.builder()
                .id(1)
                .user(user)
                .route(route)
                .status(BookingStatus.BOOKED)
                .build();
    }

    @Test
    void testGetBookingByUser_Success() {
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(bookingRepository.findByUser(user)).thenReturn(List.of(booking));

        List<Booking> result = bookingService.getBookingByUser("user1");
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findByUsername("user1");
        verify(bookingRepository, times(1)).findByUser(user);
    }

    @Test
    void testGetBookingByUser_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.getBookingByUser("unknown"));
        assertEquals("Пользователь не найден", ex.getMessage());
    }

    @Test
    void testGetAllBooking() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));
        List<Booking> result = bookingService.getAllBooking();
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void testGetBookingById_Found() {
        when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));
        Optional<Booking> result = bookingService.getBookingById(1);
        assertTrue(result.isPresent());
        assertEquals(booking, result.get());
        verify(bookingRepository, times(1)).findById(1);
    }

    @Test
    void testCreateBooking_Success() {
        when(routeRepository.findById(1)).thenReturn(Optional.of(route));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking newBooking = bookingService.createBooking(1, 1);
        assertNotNull(newBooking);
        assertEquals(BookingStatus.BOOKED, newBooking.getStatus());
        // Количество свободных мест уменьшилось (10 -> 9)
        assertEquals(9, route.getAvailableSeats());
        verify(routeRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findById(1);
        verify(routeRepository, times(1)).save(route);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_RouteNotFound() {
        when(routeRepository.findById(99)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.createBooking(99, 1));
        assertEquals("Маршрут не найден", ex.getMessage());
    }

    @Test
    void testCreateBooking_NoAvailableSeats() {
        route.setAvailableSeats(0);
        when(routeRepository.findById(1)).thenReturn(Optional.of(route));
        RuntimeException ex = assertThrows(RuntimeException.class, () -> bookingService.createBooking(1, 1));
        assertEquals("Нет свободных мест", ex.getMessage());
    }

//    @Test
//    void testCancelBooking_Success() {
//        Booking bookingToCancel = Booking.builder()
//                .id(1)
//                .user(user)
//                .route(route)
//                .status(BookingStatus.BOOKED)
//                .build();
//        when(bookingRepository.findById(1)).thenReturn(Optional.of(bookingToCancel));
//        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        int initialSeats = route.getAvailableSeats();
//        boolean result = bookingService.cancelBooking(1);
//        assertTrue(result);
//        assertEquals(BookingStatus.CANCELED, bookingToCancel.getStatus());
//        assertEquals(initialSeats + 1, route.getAvailableSeats());
//        verify(bookingRepository, times(1)).findById(1);
//        verify(routeRepository, times(1)).save(route);
//        verify(bookingRepository, times(1)).save(bookingToCancel);
//    }
//
//    @Test
//    void testCancelBooking_NotFound() {
//        when(bookingRepository.findById(99)).thenReturn(Optional.empty());
//        boolean result = bookingService.cancelBooking(99);
//        assertFalse(result);
//        verify(bookingRepository, times(1)).findById(99);
//    }
}
