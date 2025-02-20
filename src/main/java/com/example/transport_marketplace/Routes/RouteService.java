package com.example.transport_marketplace.routes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private static final String ROUTES_FILE_PATH = "src/main/resources/routes.json";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private List<Route> routes = new ArrayList<>();
    private int nextId = 1;
    public RouteService(){
        loadRoutes();
    }
    public synchronized void loadRoutes() {
        File file = new File(ROUTES_FILE_PATH);
        if(!file.exists()){
            saveRoutes();
            return;
        }
        try {
            List<Route> loadedRoutes = objectMapper.readValue(file, new TypeReference<List<Route>>() {});
            routes.clear();
            routes.addAll(loadedRoutes);
            nextId = routes.stream().mapToInt(Route::getId).max().orElse(0) + 1;
        }
        catch (IOException e){
            System.err.println("Ошибка загрузки маршрутов: " + e.getMessage());
        }

    }
    private synchronized void saveRoutes() {
        try {
            objectMapper.writeValue(new File(ROUTES_FILE_PATH), routes);
        } catch (IOException e) {
            System.err.println("Ошибка сохрания маршрутов: " + e.getMessage());
        }
    }
    public synchronized List<Route> getRoutes(){
        return new ArrayList<>(routes);
    }
    public synchronized Route addRoute(Route route){
        route.setId(nextId++);
        routes.add(route);
        saveRoutes();
        return route;
    }
    public synchronized Optional<Route> getRoutesId(int id){
        return routes.stream()
                .filter(route -> route.getId() == id)
                .findFirst();
    }


    public synchronized boolean deleteRoute(int id){
        boolean removed = routes.removeIf(route -> route.getId() == id);
        if (removed) {
            saveRoutes();
        }
        return removed;
    }
    public synchronized Route updateRoute(int id, Route updatedRoute){
        Optional<Route> existingRoute = routes.stream().filter(route -> route.getId() == id).findFirst();
        if(existingRoute.isPresent()){
            Route route = existingRoute.get();
            route.setRouteFrom(updatedRoute.getRouteFrom());
            route.setRouteTo(updatedRoute.getRouteTo());
            route.setDate(updatedRoute.getDate());
            route.setTime(updatedRoute.getTime());
            route.setArrivalTime(updatedRoute.getArrivalTime());
            route.setTransport(updatedRoute.getTransport());
            route.setPrice(updatedRoute.getPrice());
            saveRoutes();
            return route;
        }
        return null;
    }
    public List<Route> searchRoutes(String routeFrom, String routeTo, String date, String transport, String time, String arrivalTime){
        return routes.stream()
                .filter(r -> (routeFrom == null || (r.getRouteFrom() != null && r.getRouteFrom().toLowerCase().contains(routeFrom.toLowerCase()))) &&
                        (routeTo == null || (r.getRouteTo() != null && r.getRouteTo().toLowerCase().contains(routeTo.toLowerCase()))) &&
                        (date == null || r.getDate().equals(date)) &&
                        (transport == null || r.getTransport().equalsIgnoreCase(transport)) &&
                        (time == null || r.getTime().equals(time)) &&
                        (arrivalTime == null || r.getRouteTo().equals(arrivalTime)))
                .collect(Collectors.toList());
    }
}
