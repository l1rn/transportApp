package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.Booking;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    private Booking testBooking;

    @BeforeEach
    public void setUp() {
        testBooking = TestFixtures.createTestBooking();
    }

    @Test
    public void testSaveBooking() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        Booking savedBooking = bookingRepository.save(testBooking);

        assertNotNull(savedBooking);
        assertEquals(1, savedBooking.getId());

        verify(bookingRepository).save(testBooking);
    }

    @Test
    public void testGetListOfBookings() {
        Booking testBooking2 = Booking.builder()
                .id(2)
                .status(BookingStatus.CANCELLED)
                .build();
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking2);

        List<Booking> mockBookings = Arrays.asList(testBooking, testBooking2);
        when(bookingRepository.findAll()).thenReturn(mockBookings);

        List<Booking> bookings = bookingRepository.findAll();

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertEquals(BookingStatus.CANCELLED, bookings.get(1).getStatus());
        assertEquals(1, bookings.get(0).getId());

        verify(bookingRepository).findAll();
    }

    @Test
    public void givenBook_whenSaved_thenCanBeFoundById() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingRepository.findById(testBooking.getId())).thenReturn(Optional.of(testBooking));

        Booking savedBooking = bookingRepository.save(testBooking);
        Booking foundBooking = bookingRepository.findById(testBooking.getId())
                .orElse(null);

        assertNotNull(foundBooking);

        assertEquals(foundBooking.getRoute(), testBooking.getRoute());
        assertEquals(foundBooking.getCreatedAt(), testBooking.getCreatedAt());
        assertEquals(foundBooking.getStatus(), testBooking.getStatus());

        verify(bookingRepository).save(testBooking);
        verify(bookingRepository).findById(testBooking.getId());
    }

    @AfterEach
    public void tearDown(){
        reset(bookingRepository);
    }
}
