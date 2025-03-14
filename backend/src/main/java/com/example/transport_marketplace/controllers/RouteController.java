package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.dto.routes.RouteDTO;
import com.example.transport_marketplace.dto.routes.RouteRequest;
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
    public ResponseEntity<List<Route>> getRoutes() throws IOException {
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
            return ResponseEntity.badRequest().body("Invalid request: ID cannot be empty.");
        }
        try {
            int routeId = Integer.parseInt(id);
            Route route = routeService.getRouteById(routeId)
                    .orElseThrow(() -> new RouteNotFoundException(routeId));
            return new ResponseEntity<>(route, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
    }

    @Operation(
            summary = "Добавление нового маршрута",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Создаёт новый маршрут. Доступно только администраторам."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Маршрут создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RouteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/panel/add")
    public ResponseEntity<RouteDTO> addRoute(
            @Valid @RequestBody RouteRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new ValidationException(String.valueOf(result));
        }
        RouteDTO response = routeService.addRoute(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление маршрута", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/panel/delete/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable int id){
        boolean deleted = routeService.deleteRoute(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/panel/update/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable int id, @RequestBody Route updatedRoute){
        Route route = routeService.updateRoute(id, updatedRoute);
        if(route == null){
            throw new RouteNotFoundException(id);
        }
         return new ResponseEntity<>(route, HttpStatus.OK);
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
    public ResponseEntity<List<Route>> searchByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice
    ) {
        List<Route> filteredRoutes = routeService.getRoutes().stream()
                .filter(route -> route.getPrice() >= minPrice && route.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        return new ResponseEntity<>(filteredRoutes, HttpStatus.OK);
    }

}
