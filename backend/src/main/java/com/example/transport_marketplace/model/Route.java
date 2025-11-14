package com.example.transport_marketplace.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "routes")
public class Route implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "route_from", unique = false, nullable = false)
    private String routeFrom;

    @Column(name = "route_to", unique = false, nullable = false)
    private String routeTo;

    @Column(name = "transport", unique = false, nullable = false)
    private String transport;

    @Column(name = "destination_time", unique = false, nullable = false)
    private LocalDateTime destinationTime;

    @Column(name = "arrival_time", unique = false, nullable = false, updatable = true)
    private LocalDateTime arrivalTime;

    @Column(name = "available_seats", unique = false, nullable = true)
    private Integer availableSeats;

    @Column(name = "price", unique = false, nullable = false, updatable = true)
    private Double price;
}
