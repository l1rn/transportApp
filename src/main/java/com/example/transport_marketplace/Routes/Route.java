package com.example.transport_marketplace.routes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "id")
    private int id;
    @Column(name = "route-from", unique = false, nullable = false)
    private String routeFrom;
    @Column(name = "route-to", unique = false, nullable = false)
    private String routeTo;
    @Column(name = "date", unique = false, nullable = false)
    private String date;
    @Column(name = "transport", unique = false, nullable = false)
    private String transport;
    @Column(name = "time", unique = false, nullable = false)
    private String time;
    @Column(name = "arrival-time", unique = false, nullable = false, updatable = true)
    private String arrivalTime;
    @Column(name = "price", unique = false, nullable = false, updatable = true)
    private double price;
}
