package com.example.transport_marketplace;

public class Booking {
    private int id;
    private int routeId;
    private String passengerName;
    public Booking(){

    }
    public Booking(int id, int routeId, String passengerName){
        this.id = id;
        this.routeId = routeId;
        this.passengerName = passengerName;
    }
    public int getId(){return id;};
    public void setId(int id){
        this.id = id;
    }
    public int getRouteId(){return routeId;}
    public void setRouteId(int routeId){
        this.routeId = routeId;
    }
    public String getPassengerName(){return passengerName;}
    public void setPassengerName(String passengerName){
        this.passengerName = passengerName;
    }


}
