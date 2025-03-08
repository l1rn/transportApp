package com.example.transport_marketplace.security;

import com.example.transport_marketplace.jwt.JwtService;
import com.example.transport_marketplace.model.User;
import com.example.transport_marketplace.service.BookingService;
import com.example.transport_marketplace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;

@RequiredArgsConstructor
@Component("bookingSecurity")
public class BookingSecurity {
    private final BookingService bookingService;
    private final UserService userService;

    public boolean isBookingOwner(int bookingId, String username) {
        User user = userService.getByUsername(username);
        return bookingService.getBookingById(bookingId)
                .map(b -> b.getUser().getId() == user.getId())
                .orElse(false);
    }
    public boolean canAccessBooking(int bookingId, String username) {
        User user = userService.getByUsername(username);
        return isBookingOwner(bookingId, username)
                || user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
