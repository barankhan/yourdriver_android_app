package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class SupportTicket {

    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;


    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private int id;

    @SerializedName("createdAt")
    private  String createdAt;

    @SerializedName("updatedAt")
    private  String updatedAt;

    @SerializedName("closedAt")
    private  String closedAt;


    @SerializedName("userId")
    private  int userId;


    @SerializedName("rideId")
    private  int rideId;


    @SerializedName("isClosed")
    private  int isClosed;

    @SerializedName("isUnread")
    private  int isUnread;


    public int getIsUnread() {
        return isUnread;
    }

    public void setIsUnread(int isUnread) {
        this.isUnread = isUnread;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRideId() {
        return rideId;
    }

    public void setRideId(int rideId) {
        this.rideId = rideId;
    }

    public int getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(int isClosed) {
        this.isClosed = isClosed;
    }
}
