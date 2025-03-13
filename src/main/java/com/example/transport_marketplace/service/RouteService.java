package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.routes.RouteDTO;
import com.example.transport_marketplace.dto.routes.RouteRequest;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    public List<Route> getRoutes(){
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(int id){
        return routeRepository.findById(id);
    }

    public Route addRoute(Route route){
        return routeRepository.save(route);
    }

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
    public boolean deleteRoute(int id){
        if(routeRepository.existsById(id)){
            routeRepository.deleteById(id);
            return true;
        }
        return false;
    }
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
    public List<Route> searchRoutes(String routeFrom, String routeTo, String date, String transport){
        if(routeFrom == null && routeTo == null && date == null && transport == null){
            return routeRepository.findAll();
        }
        return routeRepository.searchRoutes(routeFrom, routeTo, date, transport);
    }
}