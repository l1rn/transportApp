package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.routes.RouteDTO;
import com.example.transport_marketplace.dto.routes.RouteRequest;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Cacheable(value = "allRoutes")
    public List<Route> getRoutes(){
        return routeRepository.findAll();
    }

    @Cacheable(value = "route", key = "#id")
    public Optional<Route> getRouteById(int id){
        return routeRepository.findById(id);
    }

    @Caching(
            put = @CachePut(value = "route", key = "#result.id"),
            evict = @CacheEvict(value = "allRoutes", allEntries = true)
    )
    public RouteDTO addRoute(RouteRequest request) {
        Route route = convertToEntity(request);
        Route savedRoute = routeRepository.save(route);
        return convertToDTO(savedRoute);
    }

    private Route convertToEntity(RouteRequest request) {
        return Route.builder()
                .routeFrom(request.getRouteFrom())
                .routeTo(request.getRouteTo())
                .date(request.getDate())
                .time(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .transport(request.getTransport())
                .availableSeats(request.getAvailableSeats())
                .price(request.getPrice())
                .build();
    }

    private RouteDTO convertToDTO(Route route) {
        return RouteDTO.builder()
                .id(route.getId())
                .routeFrom(route.getRouteFrom())
                .routeTo(route.getRouteTo())
                .date(route.getDate())
                .departureTime(route.getTime())
                .arrivalTime(route.getArrivalTime())
                .transport(route.getTransport())
                .availableSeats(route.getAvailableSeats())
                .price(route.getPrice())
                .build();
    }

    @Caching(evict = {
            @CacheEvict(value = "route", key = "#id"),
            @CacheEvict(value = "allRoutes", allEntries = true),
            @CacheEvict(value = "route", allEntries = true)
    })
    public boolean deleteRoute(int id){
        Optional<Route> route = routeRepository.findById(id);
        if(route.isPresent()){
            routeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Caching(
            put = @CachePut(value = "route", key = "#id"),
            evict = {
                    @CacheEvict(value = "allRoutes", allEntries = true),
                    @CacheEvict(value = "routeSearch", allEntries = true)
            }
    )
    public Route updateRoute(int id, Route updatedRoute){
        Optional<Route> existingRoute = routeRepository.findById(id);
        if(existingRoute.isPresent()){
            Route route = existingRoute.get();
            route.setRouteFrom(updatedRoute.getRouteFrom());
            route.setRouteTo(updatedRoute.getRouteTo());
            route.setDate(updatedRoute.getDate());
            route.setTime(updatedRoute.getTime());
            route.setArrivalTime(updatedRoute.getArrivalTime());
            route.setTransport(updatedRoute.getTransport());
            route.setAvailableSeats(updatedRoute.getAvailableSeats());
            route.setPrice(updatedRoute.getPrice());
            return routeRepository.save(route);
        }
        return null;
    }

    @Cacheable(value = "routeSearch", key = "#routeFrom,#routeTo,#date,#transport")
    public List<Route> searchRoutes(String routeFrom, String routeTo, String date, String transport){
        if(routeFrom == null && routeTo == null && date == null && transport == null){
            return getRoutes();
        }
        return routeRepository.searchRoutes(routeFrom, routeTo, date, transport);
    }
}