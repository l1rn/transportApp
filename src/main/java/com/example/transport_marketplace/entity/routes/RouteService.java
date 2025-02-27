package com.example.transport_marketplace.entity.routes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

//    public Page<Route> getAllRoutes(Pageable pageable){
//        return routeRepository.findAll(pageable);
//    }
    public List<Route> getRoutes(){
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(int id){
        return routeRepository.findById(id);
    }

    public Route addRoute(Route route){
        return routeRepository.save(route);
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

        return routeRepository.findAll().stream()
                .filter(r -> (routeFrom == null || (r.getRouteFrom() != null && r.getRouteFrom().toLowerCase().contains(routeFrom.toLowerCase()))) &&
                        (routeTo == null || (r.getRouteTo() != null && r.getRouteTo().toLowerCase().contains(routeTo.toLowerCase()))) &&
                        (date == null || r.getDate().equals(date)) &&
                        (transport == null || r.getTransport().equalsIgnoreCase(transport)))
                .collect(Collectors.toList());
    }
}