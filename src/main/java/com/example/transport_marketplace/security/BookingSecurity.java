package com.example.transport_marketplace.security;

import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("bookingSecurity")
public class BookingSecurity {
    private final BookingService bookingService;

    public boolean isBookingOwner(int bookingId, User user){
        return bookingService.getBookingById(bookingId)
                .map(b -> b.getUser().getId() == user.getId())
                .orElse(false);
    }
    public boolean canAccessBooking(int bookingId, User user){
        return isBookingOwner(bookingId, user)
                || user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
