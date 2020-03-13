package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class RideAlert {
    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("driverId")
    private int driverId;

    @SerializedName("rideId")
    private int rideId;

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private  String createdAt;

    @SerializedName("rejectedAt")
    private  String rejectedAt;

    @SerializedName("updatedAt")
    private  String updatedAt;

    @SerializedName("isAccepted")
    private int isAccepted;

    @SerializedName("isRejected")
    private int isRejected;

    @SerializedName("firebaseRequestReceived")
    private int firebaseRequestReceived;

    @SerializedName("driverLat")
    private double driverLat;


    @SerializedName("driverLng")
    private double driverLng;


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

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
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

    public String getRejectedAt() {
        return rejectedAt;
    }

    public void setRejectedAt(String rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }

    public int getIsRejected() {
        return isRejected;
    }

    public void setIsRejected(int isRejected) {
        this.isRejected = isRejected;
    }

    public int getFirebaseRequestReceived() {
        return firebaseRequestReceived;
    }

    public void setFirebaseRequestReceived(int firebaseRequestReceived) {
        this.firebaseRequestReceived = firebaseRequestReceived;
    }

    public double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(double driverLat) {
        this.driverLat = driverLat;
    }

    public double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(double driverLng) {
        this.driverLng = driverLng;
    }
}
