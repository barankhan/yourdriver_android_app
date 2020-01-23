package com.baran.driver.Services;

import com.baran.driver.Model.UserRide;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DriverGoogleAPI {

    @FormUrlEncoded
    @POST("accept_ride.php")
    Call<ResponseBody> getDirectionInfo(@Field("origin") String origin, @Field("destination") String destination, @Field("key")String key);



}
