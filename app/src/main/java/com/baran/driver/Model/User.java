package com.baran.driver.Model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("licence")
    private String licence;

    @SerializedName("isActive")
    private int isActive=1;

    @SerializedName("isVerified")
    private int isVerified=0;


    @SerializedName("isDriver")
    private int isDriver=0;


    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id=0;

    @SerializedName("verificationToken")
    private int verificationToken;


    @SerializedName("email")
    private String email;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("isDeleted")
    private int isDeleted=0;

    @SerializedName("driverSteps")
    private int driverSteps=0;

    @SerializedName("isDriverOnline")
    private int isDriverOnline=0;

    @SerializedName("father")
    private String father;

    @SerializedName("cnic")
    private String cnic;
    @SerializedName("cnicFront")
    private String cnicFront;
    @SerializedName("cnicRear")
    private String cnicRear;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("picture")
    private String picture;

    @SerializedName("vehicleFront")
    private String vehicleFront;

    @SerializedName("vehicleRear")
    private String vehicleRear;

    @SerializedName("registration")
    private String registration;


    @SerializedName("route")
    private String route;

    @SerializedName("regAlphabet")
    private String regAlphabet;

    @SerializedName("regYear")
    private String regYear;

    @SerializedName("regNo")
    private String regNo;

    @SerializedName("vehicleType")
    private String vehicleType;


    @SerializedName("isDriverOnTrip")
    private int isDriverOnTrip=0;


    @SerializedName("balance")
    private Double balance=0.0;


    @SerializedName("vehicleMade")
    private String vehicleMade;

    @SerializedName("vehicleColor")
    private String vehicleColor;


    @SerializedName("totalRides")
    private int totalRides;


    @SerializedName("rating")
    private double rating;

    @SerializedName("acceptancePoints")
    private int acceptancePoints;


    public int getTotalRides() {
        return totalRides;
    }

    public void setTotalRides(int totalRides) {
        this.totalRides = totalRides;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getAcceptancePoints() {
        return acceptancePoints;
    }

    public void setAcceptancePoints(int acceptancePoints) {
        this.acceptancePoints = acceptancePoints;
    }

    public String getVehicleMade() {
        return vehicleMade;
    }

    public void setVehicleMade(String vehicleMade) {
        this.vehicleMade = vehicleMade;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getVehicleFront() {
        return vehicleFront;
    }

    public void setVehicleFront(String vehicleFront) {
        this.vehicleFront = vehicleFront;
    }

    public String getVehicleRear() {
        return vehicleRear;
    }

    public void setVehicleRear(String vehicleRear) {
        this.vehicleRear = vehicleRear;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRegAlphabet() {
        return regAlphabet;
    }

    public void setRegAlphabet(String regAlphabet) {
        this.regAlphabet = regAlphabet;
    }

    public String getRegYear() {
        return regYear;
    }

    public void setRegYear(String regYear) {
        this.regYear = regYear;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getIsDriverOnTrip() {
        return isDriverOnTrip;
    }

    public void setIsDriverOnTrip(int isDriverOnTrip) {
        this.isDriverOnTrip = isDriverOnTrip;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public int getIsActive() {
        return isActive;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public int getIsDriver() {
        return isDriver;
    }

    public int getId() {
        return id;
    }

    public int getVerificationToken() {
        return verificationToken;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCreatedAt() {
        return createdAt;
    }




    public int getIsDriverOnline() {
        return isDriverOnline;
    }

    public void setIsDriverOnline(int isDriverOnline) {
        this.isDriverOnline = isDriverOnline;
    }


    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getDriverSteps() {
        return driverSteps;
    }

    public void setDriverSteps(int driverSteps) {
        this.driverSteps = driverSteps;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getCnicFront() {
        return cnicFront;
    }

    public void setCnicFront(String cnicFront) {
        this.cnicFront = cnicFront;
    }

    public String getCnicRear() {
        return cnicRear;
    }

    public void setCnicRear(String cnicRear) {
        this.cnicRear = cnicRear;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public void setIsDriver(int isDriver) {
        this.isDriver = isDriver;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVerificationToken(int verificationToken) {
        this.verificationToken = verificationToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getResponse() {
        return response;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "response='" + response + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + mobile + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
