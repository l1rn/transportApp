package com.example.transport_marketplace.controllers;

import com.example.transport_marketplace.dto.suggestions.SuggestionDTO;
import com.example.transport_marketplace.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
public class SuggestionController {
    @Autowired
    private final SuggestionService suggestionService;

    @GetMapping("/cities")
    public ResponseEntity<?> getCitySuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit){

        try{
            List<String> cities = suggestionService.findCitiesByQuery(q, limit);

            SuggestionDTO dto = SuggestionDTO
                    .builder()
                    .data(cities)
                    .count(cities.size())
                    .query(q)
                    .build();

            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch cities");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
