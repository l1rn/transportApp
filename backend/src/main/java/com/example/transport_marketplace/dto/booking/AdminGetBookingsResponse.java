package com.example.transport_marketplace.dto.booking;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Booking;

public record AdminGetBookingsResponse(
        Integer id,
        Integer routeId,
        String routeFrom,
        String routeTo,
        String destinationTime,
        String arrivalTime,
        String transport,
        String username,
        BookingStatus status,
        Double price
) {
    public static AdminGetBookingsResponse from(Booking booking){
        return new AdminGetBookingsResponse(
                booking.getId(),
                booking.getRoute().getId(),
                booking.getRoute().getRouteFrom(),
                booking.getRoute().getRouteTo(),
                booking.getRoute().getTime(),
                booking.getRoute().getArrivalTime(),
                booking.getRoute().getTransport(),
                booking.getUser().getUsername(),
                booking.getStatus(),
                booking.getRoute().getPrice()
        );
    }
}
