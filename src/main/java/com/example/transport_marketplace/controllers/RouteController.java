package com.example.transport_marketplace.controllers;
import com.example.transport_marketplace.exceptions.routes.Exceptions.BadRequestException;
import com.example.transport_marketplace.exceptions.routes.Exceptions.RouteNotFoundException;
import com.example.transport_marketplace.model.Route;
import com.example.transport_marketplace.service.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/routes")
@CrossOrigin(origins = "*")
public class RouteController {
    @Autowired
    private RouteService routeService;

    RouteController(RouteService routeService){
        this.routeService = routeService;
    }

//    @Operation(summary = "Получение всех маршрутов")
//    @GetMapping
//    public ResponseEntity<Page<Route>> getRoutes(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String sortBy
//    ){
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        Page<Route> routes = routeService.getAllRoutes(pageable);
//        return ResponseEntity.ok(routes);
//    }
    @Operation(summary = "Получение всех маршрутов")

    @GetMapping
    public ResponseEntity<List<Route>> getRoutes() throws IOException{
        List<Route> routes = routeService.getRoutes();
        return new ResponseEntity<>(routes, HttpStatus.OK);
    }

    @Operation(summary = "Поиск маршрута по id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable String id){
        if(id == null || id.isBlank()){
            return ResponseEntity.badRequest().body("Invalid request: ID cannot be empty.");
        }
        try{
            int routeId = Integer.parseInt(id);
            Route route = routeService.getRouteById(routeId)
                    .orElseThrow(() -> new RouteNotFoundException(routeId));
            return new ResponseEntity<>(route, HttpStatus.OK);
        }
        catch (NumberFormatException e){
            throw new BadRequestException();
        }
    }

    @Operation(summary = "Добавление маршрута")
    @PostMapping
    public ResponseEntity<Route> addRoute(@RequestBody Route route){
        Route savedRoute = routeService.addRoute(route);
        return new ResponseEntity<>(savedRoute, HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление маршрута")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable int id){
        boolean deleted = routeService.deleteRoute(id);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> updateRoute(@PathVariable int id, @RequestBody Route updatedRoute){
        Route route = routeService.updateRoute(id, updatedRoute);
        if(route == null){
            throw new RouteNotFoundException(id);
        }
         return new ResponseEntity<>(route, HttpStatus.OK);
    }
    @Operation(summary = "Поиск маршрутов по дате")
    @GetMapping("/searchByDate")
    public ResponseEntity<List<Route>> searchByDate(@RequestParam String date){
        List<Route> filteredRoutes = routeService.getRoutes().stream()
                .filter(route -> route.getDate().equals(date))
                .collect(Collectors.toList());
        return new ResponseEntity<>(filteredRoutes, HttpStatus.OK);
    }

    @Operation(summary = "Поиск маршрута по транспорту")
    @GetMapping("/searchByTransport")
    public ResponseEntity<List<Route>> searchByTransport(@RequestParam String transport){
        List<Route> filteredRoutes = routeService.getRoutes().stream()
                .filter(route -> route.getTransport().equals(transport))
                .collect(Collectors.toList());
        return new ResponseEntity<>(filteredRoutes, HttpStatus.OK);
    }

    @Operation(summary = "Поиск маршрутов")
    @GetMapping("/search")
    public ResponseEntity<?> searchRoutes(
            @RequestParam(required = false) String routeFrom,
            @RequestParam(required = false) String routeTo,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String transport){

        List<Route> filteredRoutes = routeService.searchRoutes(routeFrom, routeTo, date, transport);
        System.out.println("Найдено маршрутов: " + filteredRoutes.size());

        return new ResponseEntity<>(filteredRoutes, HttpStatus.OK);
    }

}
