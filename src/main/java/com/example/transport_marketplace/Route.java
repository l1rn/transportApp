package com.example.transport_marketplace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private int id;
    private String route;
    private String date;
    private String time;
    private String transport;
    private double price;
    @JsonProperty
    // id
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    // route
    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = (route == null) ? "" : route; }

    // date
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }

    // time
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }

    // transport
    public String getTransport(){
        return transport;
    }
    public void setTransport(String transport){
        this.transport = transport;
    }

    // price

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    // getters и setters
}
