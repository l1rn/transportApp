package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.dto.RouteRequest;
import com.example.transport_marketplace.dto.suggestions.SuggestionDTO;
import com.example.transport_marketplace.exceptions.FailedToUpdateModelException;
import com.example.transport_marketplace.exceptions.routes.Exceptions.RouteNotFoundException;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "Route API")
public class RouteController {
    @Autowired
    private final RouteService routeService;

    @Operation(
            summary = "Получение всех маршрутов",
            description = "Возвращает список всех доступных маршрутов. Доступно всем."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список маршрутов",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Route.class, type = "array")))
    })
    @GetMapping
    public ResponseEntity<List<Route>> getRoutes() {
        List<Route> routes = routeService.getRoutes();
        return ResponseEntity.ok(routes);
    }

    @Operation(
            summary = "Поиск маршрута по ID",
            description = "Возвращает маршрут по указанному идентификатору."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Маршрут найден",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Route.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный ID"),
            @ApiResponse(responseCode = "404", description = "Маршрут не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable(name = "id") String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().body("Айди не указан");
        }
        try {
            int routeId = Integer.parseInt(id);
            Route route = routeService.getRouteById(routeId)
                    .orElseThrow(() -> new RouteNotFoundException("Не удалось найти маршрут#" + routeId));
            return new ResponseEntity<>(route, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Невозможно найти маршрут по этому id");
        }
    }

    @Operation(
            summary = "Добавление нового маршрута",
            description = "Создаёт новый маршрут. Доступно только администраторам."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Маршрут создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Route.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/panel/add")
    public ResponseEntity<?> addRoute(@RequestBody RouteRequest request){
        try{
            routeService.addRoute(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Маршрут был создан");
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось создать маршрут" + e.getMessage());
        }
    }

    @Operation(summary = "Удаление маршрута")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/panel/delete/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable int id){
        boolean deleted = routeService.deleteRoute(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Обновление маршрута")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/panel/update/{id}")
    public ResponseEntity<?> updateRoute(
            @PathVariable int id,
            @RequestBody RouteRequest request
    ) {

        try {
            boolean updated = routeService.updateRoute(id, request);
            if(!updated){
                throw new FailedToUpdateModelException("Не удалось обновить модель!");
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Маршрут был успешно изменен");
        }
        catch (FailedToUpdateModelException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Не удалось обновить маршрут: " + e.getMessage());
        }
        catch (RouteNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Не удалось найти маршрут: " + e.getMessage());
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось обновить маршрут: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Поиск маршрутов с фильтрацией",
            description = "Ищет маршруты по заданным параметрам с поддержкой пагинации."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список маршрутов",
                content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/search")
    public ResponseEntity<?> searchRoutes(
            @RequestParam(required = false) String routeFrom,
            @RequestParam(required = false) String routeTo,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String transport,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        if (minPrice != null && maxPrice != null && minPrice > maxPrice){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Максимальная цена должна быть больше минимальной");
        }

        List<Route> filteredRoutes = routeService.searchRoutes(routeFrom, routeTo, date,
                transport, minPrice, maxPrice);

        int start = page * size;
        int end = Math.min(start + size, filteredRoutes.size());

        List<Route> paginatedResult = start < end
                ? filteredRoutes.subList(start, end)
                : List.of();

        return new ResponseEntity<>(
                new HashMap<String, Object>(){{
                    put("content", paginatedResult);
                    put("totalElements", filteredRoutes.size());
                    put("totalPages", (int) Math.ceil((double) filteredRoutes.size() / size));
                    put("currentPage", page);
                }},
                HttpStatus.OK
        );
    }

    @GetMapping("/s/cities-to")
    public ResponseEntity<?> getCitiesToSuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit){

        try{
            return ResponseEntity.ok(routeService.findCitiesToByQuery(q, limit));
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch cities");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/s/cities-from")
    public ResponseEntity<?> getCitiesFromSuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int limit){
        try{
            return ResponseEntity.ok(routeService.findCitiesFromByQuery(q, limit));
        }
        catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch cities");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
