package com.baran.driver.Model;

import com.google.gson.annotations.SerializedName;

public class Ride {

    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("driver_id")
    private int driverId;

    @SerializedName("passengerId")
    private int passengerId;

    @SerializedName("pickup_lat")
    private long pickupLat;

    @SerializedName("pickup_lng")
    private long pickupLng;

    @SerializedName("vehicle_type")
    private String vehicleType;

    @SerializedName("dropoff_lat")
    private long dropoffLat;

    @SerializedName("dropoff_lng")
    private long dropoffLng;

    @SerializedName("id")
    private int id;



    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public long getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(long pickupLat) {
        this.pickupLat = pickupLat;
    }

    public long getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(long pickupLng) {
        this.pickupLng = pickupLng;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public long getDropoffLat() {
        return dropoffLat;
    }

    public void setDropoffLat(long dropoffLat) {
        this.dropoffLat = dropoffLat;
    }

    public long getDropoffLng() {
        return dropoffLng;
    }

    public void setDropoffLng(long dropoffLng) {
        this.dropoffLng = dropoffLng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
