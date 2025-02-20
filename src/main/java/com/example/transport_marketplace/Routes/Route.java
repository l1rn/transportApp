package com.example.transport_marketplace.routes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private int id;
    private String routeFrom;
    private String routeTo;
    private String date;
    private String transport;
    private String time;
    private String arrivalTime;
    private double price;

    public Route(){}

    public Route(int id, String routeFrom, String routeTo, String date, String transport, String time, String arrivalTime, double price){
        this.id = id;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.date = date;
        this.transport = transport;
        this.time = time;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }

    @JsonProperty
    // id
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    @JsonProperty
    public String getRouteFrom() { return routeFrom; }
    public void setRouteFrom(String routeFrom) {
        this.routeFrom = (routeFrom == null) ? "" : routeFrom;
    }

    @JsonProperty
    public String getRouteTo(){return routeTo;}

    public void setRouteTo(String routeTo){
        this.routeTo = (routeTo == null) ? "" : routeTo;
    }

    @JsonProperty
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    @JsonProperty
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }

    @JsonProperty
    public String getArrivalTime(){
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime){
        this.arrivalTime = arrivalTime;
    }
    @JsonProperty
    // transport
    public String getTransport(){
        return transport;
    }
    public void setTransport(String transport){
        this.transport = transport;
    }

    // price
    @JsonProperty
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    // getters и setters
}
