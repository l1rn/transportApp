package com.example.transport_marketplace.config;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Booking;
import com.example.transport_marketplace.repo.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleTasks {
    @Autowired
    private final BookingRepository bookingRepository;
    @Scheduled(fixedRate = 3600000)
    public void deleteBookingByOneDay() {
        List<Booking> canceledBookings = bookingRepository.findAllByStatus(BookingStatus.CANCELED);
        if(!canceledBookings.isEmpty()){
            bookingRepository.deleteAll(canceledBookings);
        }
    }
}
