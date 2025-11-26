package com.example.transport_marketplace.service;

import com.example.transport_marketplace.dto.RouteRequest;
import com.example.transport_marketplace.dto.suggestions.SuggestionDTO;
import com.example.transport_marketplace.exceptions.routes.RouteNotFoundException;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public void addRoute(RouteRequest request) {
        Route route =  Route.builder()
                .routeFrom(request.getRouteFrom())
                .routeTo(request.getRouteTo())
                .transport(request.getTransport())
                .destinationTime(request.getDestinationTime())
                .arrivalTime(request.getArrivalTime())
                .availableSeats(request.getAvailableSeats())
                .price(request.getPrice())
                .build();
        routeRepository.save(route);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "route", allEntries = true),
                    @CacheEvict(value = "routes", allEntries = true)
            }
    )
    public void deleteRoute(Integer id){
        routeRepository.deleteById(id);
    }

    @Transactional
    public boolean updateRoute(int id, RouteRequest request){
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Маршрут не был найден #" + id));

        if(route != null){
            route.setRouteFrom(request.getRouteFrom());
            route.setRouteTo(request.getRouteTo());
            route.setTransport(request.getTransport());
            route.setAvailableSeats(request.getAvailableSeats());
            route.setArrivalTime(request.getArrivalTime());
            route.setDestinationTime(request.getDestinationTime());
            route.setPrice(request.getPrice());
            routeRepository.save(route);
            return true;
        }
        return false;
    }

    public List<Route> searchRoutes(String routeFrom,
                                    String routeTo,
                                    String transport,
                                    Double minPrice,
                                    Double maxPrice){
        if(routeFrom == null &&
                routeTo == null &&
                transport == null &&
                minPrice == null &&
                maxPrice == null) {
            return getRoutes();
        }
        return routeRepository.searchRoutes(routeFrom, routeTo, transport, minPrice, maxPrice);
    }

    public SuggestionDTO findCitiesToByQuery(String query, int limit){
        String lowerQuery = query.toLowerCase(Locale.ROOT);
        List<String> routes = routeRepository.findAll()
                .stream()
                .map(Route::getRouteTo)
                .distinct()
                .filter(c -> c.toLowerCase(Locale.ROOT).contains(lowerQuery))
                .sorted(Comparator
                        .comparing((String city) ->
                                !city.toLowerCase(Locale.ROOT).startsWith(lowerQuery))
                        .thenComparing(String::compareToIgnoreCase)
                )
                .limit(limit)
                .collect(Collectors.toList());

        return SuggestionDTO
                .builder()
                .data(routes)
                .limit(routes.size())
                .query(lowerQuery)
                .build();
    }

    public SuggestionDTO findCitiesFromByQuery(String query, int limit){
        String lowerQuery = query.toLowerCase(Locale.ROOT);
        List<String> routes = routeRepository.findAll()
                .stream()
                .map(Route::getRouteFrom)
                .distinct()
                .filter(c -> c.toLowerCase(Locale.ROOT).contains(lowerQuery))
                .sorted(Comparator
                        .comparing((String city) ->
                                !city.toLowerCase(Locale.ROOT).startsWith(lowerQuery))
                        .thenComparing(String::compareToIgnoreCase)
                )
                .limit(limit)
                .collect(Collectors.toList());

        return SuggestionDTO
                .builder()
                .data(routes)
                .limit(routes.size())
                .query(lowerQuery)
                .build();
    }
}