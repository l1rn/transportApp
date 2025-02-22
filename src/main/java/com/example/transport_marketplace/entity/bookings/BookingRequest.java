package com.example.transport_marketplace.entity.bookings;

import com.example.transport_marketplace.entity.routes.Route;
import com.example.transport_marketplace.entity.users.User;
import lombok.Data;

@Data
public class BookingRequest {
    private int routeId;
    private int userId;
}
