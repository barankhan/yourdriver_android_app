package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class DriverServerResponse {

    @SerializedName("response")
    private String response;


    @SerializedName("message")
    private String message;



    @SerializedName("driver_id")
    private String driverId;


    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
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
