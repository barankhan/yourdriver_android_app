package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class Ride {

    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("driverId")
    private int driverId;

    @SerializedName("passengerId")
    private int passengerId;

    @SerializedName("pickupLat")
    private Double pickupLat;

    @SerializedName("pickupLng")
    private Double pickupLng;

    @SerializedName("vehicleType")
    private String vehicleType;

    @SerializedName("dropoffLat")
    private Double dropoffLat;

    @SerializedName("dropoffLng")
    private Double dropoffLng;

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private  String createdAt;

    @SerializedName("updatedAt")
    private  String updatedAt;


    @SerializedName("isRideStarted")
    private  int isRideStarted=0;

    @SerializedName("isRideCancelled")
    private  int isRideCancelled=0;

    @SerializedName("isDriverArrived")
    private  int isDriverArrived=0;

    @SerializedName("isRideEnded")
    private  int isRideEnded=0;

    @SerializedName("cancelledByTypeId")
    private  int cancelledByTypeId=0;

    @SerializedName("rideStartedAt")
    private  String rideStartedAt;

    @SerializedName("rideCancelledAt")
    private  String rideCancelledAt;

    @SerializedName("rideEndedAt")
    private  String rideEndedAt;

    @SerializedName("driverArrivedAt")
    private  String driverArrivedAt;

    @SerializedName("driverLat")
    private  Double driverLat;

    @SerializedName("driverLng")
    private  Double driverLng;

    @SerializedName("distance")
    private  Double distance;


    @SerializedName("rating")
    private  int rating;


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
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

    public int getIsRideStarted() {
        return isRideStarted;
    }

    public void setIsRideStarted(int isRideStarted) {
        this.isRideStarted = isRideStarted;
    }

    public int getIsRideCancelled() {
        return isRideCancelled;
    }

    public void setIsRideCancelled(int isRideCancelled) {
        this.isRideCancelled = isRideCancelled;
    }

    public int getIsDriverArrived() {
        return isDriverArrived;
    }

    public void setIsDriverArrived(int isDriverArrived) {
        this.isDriverArrived = isDriverArrived;
    }

    public int getIsRideEnded() {
        return isRideEnded;
    }

    public void setIsRideEnded(int isRideEnded) {
        this.isRideEnded = isRideEnded;
    }

    public int getCancelledByTypeId() {
        return cancelledByTypeId;
    }

    public void setCancelledByTypeId(int cancelledByTypeId) {
        this.cancelledByTypeId = cancelledByTypeId;
    }

    public String getRideStartedAt() {
        return rideStartedAt;
    }

    public void setRideStartedAt(String rideStartedAt) {
        this.rideStartedAt = rideStartedAt;
    }

    public String getRideCancelledAt() {
        return rideCancelledAt;
    }

    public void setRideCancelledAt(String rideCancelledAt) {
        this.rideCancelledAt = rideCancelledAt;
    }

    public String getRideEndedAt() {
        return rideEndedAt;
    }

    public void setRideEndedAt(String rideEndedAt) {
        this.rideEndedAt = rideEndedAt;
    }

    public String getDriverArrivedAt() {
        return driverArrivedAt;
    }

    public void setDriverArrivedAt(String driverArrivedAt) {
        this.driverArrivedAt = driverArrivedAt;
    }

    public Double getDriverLat() {
        return driverLat;
    }

    public void setDriverLat(Double driverLat) {
        this.driverLat = driverLat;
    }

    public Double getDriverLng() {
        return driverLng;
    }

    public void setDriverLng(Double driverLng) {
        this.driverLng = driverLng;
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

    public Double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(Double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public Double getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(Double pickupLng) {
        this.pickupLng = pickupLng;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getDropoffLat() {
        return dropoffLat;
    }

    public void setDropoffLat(Double dropoffLat) {
        this.dropoffLat = dropoffLat;
    }

    public Double getDropoffLng() {
        return dropoffLng;
    }

    public Double setDropoffLng(Double dropoffLng) {
        return this.dropoffLng = dropoffLng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
