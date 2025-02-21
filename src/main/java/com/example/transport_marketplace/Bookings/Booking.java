package com.example.transport_marketplace.bookings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "route-id", nullable = false)
    private int routeId;
    @Column(name = "passenger-name", unique = true, nullable = false)
    private String passengerName;

}
