package com.example.transport_marketplace.service;

import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
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
                 .distinct()
                 .sorted(Comparator
                         .comparing((String city) -> !city.toLowerCase().startsWith(lowerQuery))
                         .thenComparing(String::compareToIgnoreCase)
                 )
                 .collect(Collectors.toList());
     }

    public List<String> findTransportUnitsByQuery(String q, int limit){
         String lQuery = q.toLowerCase();
         return routeRepository.findAll()
                 .stream()
                 .filter(r -> r.getTransport().toLowerCase().contains(lQuery))
                 .limit(limit)
                 .map(Route::getTransport)
                 .distinct()
                 .sorted(Comparator
                         .comparing((String t) -> !t.toLowerCase().startsWith(lQuery))
                         .thenComparing(String::compareToIgnoreCase)
                 )
                 .collect(Collectors.toList());
     }
}
