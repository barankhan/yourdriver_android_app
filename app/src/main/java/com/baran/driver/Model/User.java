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

    public String getIsDriver() {
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
    private int isDeleted;

    @SerializedName("isActive")
    private int isActive;

    @SerializedName("isVerified")
    private int isVerified;


    @SerializedName("isDriver")
    private String isDriver;


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
