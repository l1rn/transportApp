package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.fixtures.TestFixtures;
import com.example.transport_marketplace.model.Booking;
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
public class BookingRepositoryTest {

    @Mock
    private BookingRepository bookingRepository;

    private Booking testBooking;

    @BeforeEach
    public void setUp() {
        testBooking = TestFixtures.createTestBooking();
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
