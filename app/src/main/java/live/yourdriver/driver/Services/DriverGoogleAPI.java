package live.yourdriver.driver.Services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DriverGoogleAPI {

    @FormUrlEncoded
    @POST("json/")
    Call<ResponseBody> getDirectionInfo(@Field("origin") String origin, @Field("destination") String destination, @Field("key")String key);



}
