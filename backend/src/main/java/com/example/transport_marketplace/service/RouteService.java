package com.example.transport_marketplace.service;

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

    @CachePut(value = "route")
    public List<Route> getRoutes(){
        return routeRepository.findAll();
    }

    @Cacheable(value = "route", key = "#id")
    public Optional<Route> getRouteById(int id){
        return routeRepository.findById(id);
    }

    @CachePut(value = "route", key = "#route.id")
    public Route addRoute(Route route) {
        return routeRepository.save(route);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "route", allEntries = true),
                    @CacheEvict(value = "routes", allEntries = true)
            }
    )
    public boolean deleteRoute(int id){
        Optional<Route> route = routeRepository.findById(id);
        if(route.isPresent()){
            routeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @CachePut(value = "route", key = "#id")
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

    @Cacheable(value = "routes")
    public List<Route> searchRoutes(String routeFrom, String routeTo, String date, String transport){
        if(routeFrom == null && routeTo == null && date == null && transport == null){
            return getRoutes();
        }
        return routeRepository.searchRoutes(routeFrom, routeTo, date, transport);
    }
}