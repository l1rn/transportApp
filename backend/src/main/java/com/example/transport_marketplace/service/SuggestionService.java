package com.example.transport_marketplace.service;

import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    @Autowired
    private final RouteRepository routeRepository;

     public List<String> findCitiesByQuery(String query, int limit){
         String lowerQuery = query.toLowerCase();
         return routeRepository.findAll()
                 .stream()
                 .filter(r -> r.getRouteFrom().toLowerCase().contains(lowerQuery))
                 .limit(limit)
                 .map(Route::getRouteFrom)
                 .collect(Collectors.toList());
     }


}
