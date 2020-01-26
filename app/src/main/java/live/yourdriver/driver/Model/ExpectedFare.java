package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpectedFare {

    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("vehicleTypesList")
    private List<DriverType> vehicleTypesList;


    @SerializedName("duration")
    private String duration;


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

    public List<DriverType> getVehicleTypesList() {
        return vehicleTypesList;
    }

    public void setVehicleTypesList(List<DriverType> vehicleTypesList) {
        this.vehicleTypesList = vehicleTypesList;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
