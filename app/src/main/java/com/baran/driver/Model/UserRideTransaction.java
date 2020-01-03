package com.baran.driver.Model;

import com.google.gson.annotations.SerializedName;

public class UserRideTransaction {
    @SerializedName("user")
    private User user;

    @SerializedName("ride")
    private Ride ride;

    @SerializedName("driver_transaction")
    private DriverTransaction driverTransaction;

    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public DriverTransaction getDriverTransaction() {
        return driverTransaction;
    }

    public void setDriverTransaction(DriverTransaction driverTransaction) {
        this.driverTransaction = driverTransaction;
    }

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
}
