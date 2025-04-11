package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.exceptions.routes.Exceptions.BadRequestException;
import com.example.transport_marketplace.exceptions.routes.Exceptions.RouteNotFoundException;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/routes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
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
        return new ResponseEntity<>(routes, HttpStatus.OK);
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
                    .orElseThrow(() -> new RouteNotFoundException(routeId));
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
    public ResponseEntity<?> addRoute(
            @RequestBody Route route)
    {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(routeService.addRoute(route));
        }catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Не удалось создать маршрут");
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
    public ResponseEntity<?> updateRoute(@PathVariable int id, @RequestBody Route updatedRoute){

        try {
            Route route = routeService.updateRoute(id, updatedRoute);
            if(route == null){
                throw new RouteNotFoundException(id);
            }
            return ResponseEntity.status(HttpStatus.OK).body(route);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не удалось обновить маршут");
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        List<Route> filteredRoutes = routeService.searchRoutes(routeFrom, routeTo, date, transport);

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

    @GetMapping("/priceRange")
    public ResponseEntity<?> searchByPriceRange(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        try {
            if(minPrice == null && maxPrice == null){
                return ResponseEntity.ok(routeService.getRoutes());
            }
            else if(minPrice == null){
                List<Route> filteredByMax = routeService.getRoutes().stream()
                        .filter(route -> route.getPrice() <= maxPrice)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(filteredByMax);
            }
            else if(maxPrice == null){
                List<Route> filteredByMin = routeService.getRoutes().stream()
                        .filter(route -> route.getPrice() >= minPrice)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(filteredByMin);
            }
            else{
                List<Route> filteredRoutes = routeService.getRoutes().stream()
                        .filter(route -> route.getPrice() >= minPrice && route.getPrice() <= maxPrice)
                        .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.OK).body(filteredRoutes);
            }

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невозможно найти маршрут по данным ценам");
        }
    }
}
