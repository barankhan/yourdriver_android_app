package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class UserTransaction {
    @SerializedName("user")
    private User user;


    @SerializedName("transaction")
    private DriverTransaction transaction;



    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DriverTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(DriverTransaction transaction) {
        this.transaction = transaction;
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
}
