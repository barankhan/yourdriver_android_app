package com.baran.driver.Model;

import com.google.gson.annotations.SerializedName;

public class TransactionLiability {

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private  String createdAt;


    @SerializedName("updatedAt")
    private  String updatedAt;


    @SerializedName("title")
    private  String title;

    @SerializedName("transactionId")
    private int transactionId;


    @SerializedName("amount")
    private double amount;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
