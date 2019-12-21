package com.baran.driver.Model;

import com.google.gson.annotations.SerializedName;

public class RidePath {


    @SerializedName("lat")
    private Double Lat;

    @SerializedName("lng")
    private Double Lng;


    @SerializedName("id")
    private int id;

    @SerializedName("rideId")
    private  int rideId;

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }
}
