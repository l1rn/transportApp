package com.example.transport_marketplace.entity.routes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findByRouteFromAndRouteToAndDate(String routeFrom, String routeTo, SecureRandom date);
}
