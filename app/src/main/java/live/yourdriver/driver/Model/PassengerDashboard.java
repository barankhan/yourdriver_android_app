package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class PassengerDashboard {
    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("Balance")
    private  String Balance;

    @SerializedName("ReferralCode")
    private  String ReferralCode;

    @SerializedName("ReferredUsers")
    private  String ReferredUsers;

    @SerializedName("ReferredDrivers")
    private  String ReferredDrivers;


    public String getReferralCode() {
        return ReferralCode;
    }

    public void setReferralCode(String referralCode) {
        ReferralCode = referralCode;
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

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getReferredUsers() {
        return ReferredUsers;
    }

    public void setReferredUsers(String referredUsers) {
        ReferredUsers = referredUsers;
    }

    public String getReferredDrivers() {
        return ReferredDrivers;
    }

    public void setReferredDrivers(String referredDrivers) {
        ReferredDrivers = referredDrivers;
    }
}
