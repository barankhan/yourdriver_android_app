package com.baran.driver.Model;

import com.google.gson.annotations.SerializedName;

public class User {

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

    @SerializedName("isDeleted")
    private int isDeleted=0;

    @SerializedName("driverSteps")
    private int driverSteps=0;

    @SerializedName("father")
    private String father;

    @SerializedName("cnic")
    private String cnic;
    @SerializedName("cnicFront")
    private String cnicFront;
    @SerializedName("cnicRear")
    private String cnicRear;

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
