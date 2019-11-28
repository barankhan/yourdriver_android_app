package com.baran.driver.Services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.User;

public interface ServiceApi {
    @FormUrlEncoded
    @POST("register.php")
    Call<User> doRegistration(@Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("password") String password, @Field("firebaseToken") String firebaseToken);

    @FormUrlEncoded
    @POST("login.php")
    Call<User> doLogin(@Field("mobile") String mobile, @Field("password") String password);



    @FormUrlEncoded
    @POST("token_verification.php")
    Call<User> doTokenVerification(@Field("mobile") String mobile, @Field("verificationToken") int verificationToken);


    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<DriverServerResponse> doSendPasswordViaSMS(@Field("mobile") String mobile);

}
