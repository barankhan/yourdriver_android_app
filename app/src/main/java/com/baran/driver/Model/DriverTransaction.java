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


//    $companyHead,$headAmount,$payableAmount


    @SerializedName("payableAmount")
    private Double payableAmount=0.0;


    @SerializedName("inwardHeadAmount")
    private Double inwardHeadAmount;

    @SerializedName("companyInwardHead")
    private  String companyInwardHead;



    @SerializedName("outwardHeadAmount")
    private Double outwardHeadAmount;

    @SerializedName("companyOutwardHead")
    private  String companyOutwardHead;


    @SerializedName("transactionCompleted")
    private int transactionCompleted;

    @SerializedName("driverInitialBalance")
    private double driverInitialBalance;

    @SerializedName("passengerInitialBalance")
    private double passengerInitialBalance;


    public int getTransactionCompleted() {
        return transactionCompleted;
    }

    public void setTransactionCompleted(int transactionCompleted) {
        this.transactionCompleted = transactionCompleted;
    }

    public double getDriverInitialBalance() {
        return driverInitialBalance;
    }

    public void setDriverInitialBalance(double driverInitialBalance) {
        this.driverInitialBalance = driverInitialBalance;
    }

    public double getPassengerInitialBalance() {
        return passengerInitialBalance;
    }

    public void setPassengerInitialBalance(double passengerInitialBalance) {
        this.passengerInitialBalance = passengerInitialBalance;
    }

    public Double getOutwardHeadAmount() {
        return outwardHeadAmount;
    }

    public void setOutwardHeadAmount(Double outwardHeadAmount) {
        this.outwardHeadAmount = outwardHeadAmount;
    }

    public String getCompanyOutwardHead() {
        return companyOutwardHead;
    }

    public void setCompanyOutwardHead(String companyOutwardHead) {
        this.companyOutwardHead = companyOutwardHead;
    }

    public Double getInwardHeadAmount() {
        return inwardHeadAmount;
    }

    public void setInwardHeadAmount(Double inwardHeadAmount) {
        this.inwardHeadAmount = inwardHeadAmount;
    }

    public String getCompanyInwardHead() {
        return companyInwardHead;
    }

    public void setCompanyInwardHead(String companyInwardHead) {
        this.companyInwardHead = companyInwardHead;
    }

    public Double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(Double payableAmount) {
        this.payableAmount = payableAmount;
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
