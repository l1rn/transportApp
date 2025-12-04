package com.example.transport_marketplace.repo;

import com.example.transport_marketplace.model.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    @Query(value = "SELECT * FROM routes r WHERE " +
            "(:routeFrom IS NULL OR r.route_from ILIKE '%' || :routeFrom || '%') AND " +
            "(:routeTo IS NULL OR r.route_to ILIKE '%' || :routeTo || '%') AND " +
            "(:transport IS NULL OR r.transport ILIKE :transport)",
            nativeQuery = true)
    Page<Route> searchRoutes(
            @Param("routeFrom") String routeFrom,
            @Param("routeTo") String routeTo,
            @Param("transport") String transport,
            Pageable pageable
    );

    Route findByRouteFromAndRouteToAndDestinationTime(String routeFrom, String routeTo, LocalDateTime destinationTime);
    boolean existsByRouteFromAndRouteToAndDestinationTime(String routeFrom, String routeTo, LocalDateTime destinationTime);
}
