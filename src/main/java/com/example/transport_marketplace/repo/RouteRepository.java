package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    @Query("SELECT r FROM Route r WHERE " +
            "(:routeFrom IS NULL OR LOWER(r.routeFrom) LIKE LOWER(CONCAT('%', :routeFrom, '%'))) AND " +
            "(:routeTo IS NULL OR LOWER(r.routeTo) LIKE LOWER(CONCAT('%', :routeTo, '%'))) AND " +
            "(:date IS NULL OR r.date = :date) AND " +
            "(:transport IS NULL OR LOWER(r.transport) = LOWER(:transport))")
    List<Route> searchRoutes(
            @Param("routeFrom") String routeFrom,
            @Param("routeTo") String routeTo,
            @Param("date") String date,
            @Param("transport") String transport);

    Route findByRouteFromAndRouteToAndDate(String routeFrom, String routeTo, String date);
    boolean existsByRouteFromAndRouteToAndDate(String routeFrom, String routeTo, String date);
}
