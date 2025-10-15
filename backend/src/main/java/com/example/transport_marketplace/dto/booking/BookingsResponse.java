package com.example.transport_marketplace.dto.booking;

import com.example.transport_marketplace.enums.BookingStatus;
import com.example.transport_marketplace.model.Payment;
import com.example.transport_marketplace.model.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingsResponse {
    private int id;
    private int userId;
    private Route route;
    private List<Payment> payments;
    private BookingStatus status;
    private Payment successfulPayment;
}
