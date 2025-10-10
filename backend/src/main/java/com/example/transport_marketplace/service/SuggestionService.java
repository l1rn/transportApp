package com.example.transport_marketplace.service;

import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.repo.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    @Autowired
    private final RouteRepository routeRepository;

     public List<String> findCitiesByQuery(String query, int limit){
         String lowerQuery = query.toLowerCase(Locale.ROOT);
         return routeRepository.findAll()
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
     }
}
