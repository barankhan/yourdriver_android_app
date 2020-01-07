package com.baran.driver.Model;

import com.baran.driver.Activity.MainActivity;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deathcode on 26/01/18.
 */

public class ChatMessage {


    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("id")
    private int id;

    @SerializedName("senderId")
    private int senderId;

    @SerializedName("rideId")
    private int rideId;

    @SerializedName("createdAt")
    private  String createdAt;

    @SerializedName("updatedAt")
    private  String updatedAt;


    private int currentUserId = MainActivity.appPreference.getUserObjectWithoutUserValidation().getId();

    boolean isMe;

    public ChatMessage(String message, int senderId,int currentUserId ) {
        this.message = message;
        this.senderId=senderId;
        setMe(currentUserId);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
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







    public boolean isMe() {
        return isMe;
    }

    public void setMe(int currentUserId) {
        if(this.senderId!=currentUserId)
            isMe=false;
        else
            isMe=true;
    }
}
