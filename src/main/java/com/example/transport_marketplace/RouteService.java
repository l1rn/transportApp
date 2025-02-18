package com.example.transport_marketplace;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.RouteMatcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private static final String ROUTES_FILE_PATH = "src/main/resources/routes.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
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
            route.setRoute(updatedRoute.getRoute());
            route.setDate(updatedRoute.getDate());
            route.setTime(updatedRoute.getTime());
            route.setTransport(updatedRoute.getTransport());
            route.setPrice(updatedRoute.getPrice());
            saveRoutes();
            return route;
        }
        return null;
    }
    public List<Route> searchRoutes(String route, String date, String transport, String timeFrom, String timeTo){
        return routes.stream()
                .filter(r -> (route == null || (r.getRoute() != null && r.getRoute().toLowerCase().contains(route.toLowerCase()))) &&
                        (date == null || r.getDate().equals(date)) &&
                        (transport == null || r.getTransport().equalsIgnoreCase(transport)) &&
                        (timeFrom == null || timeTo == null || isTimeInRange(r.getTime(), timeFrom, timeTo)))
                .collect(Collectors.toList());
    }
    private boolean isTimeInRange(String time, String timeFrom, String timeTo){
        return time.compareTo(timeFrom) >= 0 && time.compareTo(timeTo) <= 0;
    }
}
