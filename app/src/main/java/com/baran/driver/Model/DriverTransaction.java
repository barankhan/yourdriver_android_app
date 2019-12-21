package com.baran.driver.Model;

import com.google.gson.annotations.SerializedName;

public class DriverTransaction {

    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("driverId")
    private int driverId;

    @SerializedName("passengerId")
    private int passengerId;

    @SerializedName("driverBalance")
    private Double driverBalance;

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private  String createdAt;


    @SerializedName("updatedAt")
    private  String updatedAt;

    @SerializedName("amountReceivedAt")
    private  String amountReceivedAt;

    @SerializedName("transactionType")
    private  String transactionType;

    @SerializedName("driverStartUpFare")
    private Double driverStartUpFare;


    @SerializedName("companyServiceCharges")
    private Double companyServiceCharges;

    @SerializedName("timeElapsedMinutes")
    private Double timeElapsedMinutes=0.0;

    @SerializedName("timeElapsedRate")
    private Double timeElapsedRate;

    @SerializedName("kmTravelled")
    private Double kmTravelled=0.0;

    @SerializedName("kmTravelledRate")
    private Double kmTravelledRate;

    @SerializedName("totalFare")
    private Double totalFare=0.0;

    @SerializedName("amountReceived")
    private Double amountReceived=0.0;

    @SerializedName("rideId")
    private int rideId;

    @SerializedName("totalAmount")
    private Double totalAmount=0.0;


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

    public Double getDriverBalance() {
        return driverBalance;
    }

    public void setDriverBalance(Double driverBalance) {
        this.driverBalance = driverBalance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAmountReceivedAt() {
        return amountReceivedAt;
    }

    public void setAmountReceivedAt(String amountReceivedAt) {
        this.amountReceivedAt = amountReceivedAt;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getDriverStartUpFare() {
        return driverStartUpFare;
    }

    public void setDriverStartUpFare(Double driverStartUpFare) {
        this.driverStartUpFare = driverStartUpFare;
    }

    public Double getCompanyServiceCharges() {
        return companyServiceCharges;
    }

    public void setCompanyServiceCharges(Double companyServiceCharges) {
        this.companyServiceCharges = companyServiceCharges;
    }

    public Double getTimeElapsedMinutes() {
        return timeElapsedMinutes;
    }

    public void setTimeElapsedMinutes(Double timeElapsedMinutes) {
        this.timeElapsedMinutes = timeElapsedMinutes;
    }

    public Double getTimeElapsedRate() {
        return timeElapsedRate;
    }

    public void setTimeElapsedRate(Double timeElapsedRate) {
        this.timeElapsedRate = timeElapsedRate;
    }

    public Double getKmTravelled() {
        return kmTravelled;
    }

    public void setKmTravelled(Double kmTravelled) {
        this.kmTravelled = kmTravelled;
    }

    public Double getKmTravelledRate() {
        return kmTravelledRate;
    }

    public void setKmTravelledRate(Double kmTravelledRate) {
        this.kmTravelledRate = kmTravelledRate;
    }

    public Double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    public Double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(Double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
