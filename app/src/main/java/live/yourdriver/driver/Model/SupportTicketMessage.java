package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class SupportTicketMessage {

    @SerializedName("id")
    private int id;

    @SerializedName("supportTicketId")
    private int supportTicketId;

    @SerializedName("createdAt")
    private  String createdAt;

    @SerializedName("updatedAt")
    private  String updatedAt;

    @SerializedName("userId")
    private  int userId;


    @SerializedName("message")
    private String message;


    @SerializedName("isMe")
    boolean isMe;


    public SupportTicketMessage(String message, int senderId,int currentUserId ) {
        this.message = message;
        this.userId=senderId;
        setMe(currentUserId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupportTicketId() {
        return supportTicketId;
    }

    public void setSupportTicketId(int supportTicketId) {
        this.supportTicketId = supportTicketId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(int currentUserId) {
        if(this.userId!=currentUserId)
            isMe=false;
        else
            isMe=true;
    }


}

