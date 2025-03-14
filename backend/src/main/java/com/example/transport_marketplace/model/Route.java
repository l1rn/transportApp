package com.example.transport_marketplace.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "route_from", unique = false, nullable = false)
    private String routeFrom;
    @Column(name = "route_to", unique = false, nullable = false)
    private String routeTo;
    @Column(name = "date", unique = false, nullable = false)
    private String date;
    @Column(name = "transport", unique = false, nullable = false)
    private String transport;
    @Column(name = "time", unique = false, nullable = false)
    private String time;
    @Column(name = "arrival_time", unique = false, nullable = false, updatable = true)
    private String arrivalTime;
    @Column(name = "available_seats", unique = false, nullable = true)
    private int availableSeats;
    @Column(name = "price", unique = false, nullable = false, updatable = true)
    private double price;

}
