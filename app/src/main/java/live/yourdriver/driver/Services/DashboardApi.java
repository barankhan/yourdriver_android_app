package live.yourdriver.driver.Services;

import live.yourdriver.driver.Model.Dashboard;
import live.yourdriver.driver.Model.DriverServerResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DashboardApi {
    @FormUrlEncoded
    @POST("dash_info.php")
    Call<Dashboard> getData(@Field("driver_id") int driver_id);
}
