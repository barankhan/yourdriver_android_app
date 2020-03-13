package live.yourdriver.driver.Model;

import com.google.gson.annotations.SerializedName;

public class Dashboard {
    @SerializedName("response")
    private String response;

    @SerializedName("message")
    private String message;

    @SerializedName("Balance")
    private  String Balance;

    @SerializedName("Last7DaysEarning")
    private  String Last7DaysEarning;

    @SerializedName("Last30DaysEarning")
    private  String Last30DaysEarning ;

    @SerializedName("ReferredUsers")
    private  String ReferredUsers;

    @SerializedName("ReferredDrivers")
    private  String ReferredDrivers;

    @SerializedName("ReferralCode")
    private  String ReferralCode;

    @SerializedName("DashTodayEarning")
    private  String DashTodayEarning;


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

    public String getLast7DaysEarning() {
        return Last7DaysEarning;
    }

    public void setLast7DaysEarning(String last7DaysEarning) {
        Last7DaysEarning = last7DaysEarning;
    }

    public String getLast30DaysEarning() {
        return Last30DaysEarning;
    }

    public void setLast30DaysEarning(String last30DaysEarning) {
        Last30DaysEarning = last30DaysEarning;
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

    public String getReferralCode() {
        return ReferralCode;
    }

    public void setReferralCode(String referralCode) {
        ReferralCode = referralCode;
    }

    public String getDashTodayEarning() {
        return DashTodayEarning;
    }

    public void setDashTodayEarning(String dashTodayEarning) {
        DashTodayEarning = dashTodayEarning;
    }
}
