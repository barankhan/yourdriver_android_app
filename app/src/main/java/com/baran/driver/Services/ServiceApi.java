package com.baran.driver.Services;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.User;

public interface ServiceApi {
    @FormUrlEncoded
    @POST("register.php")
    Call<User> doRegistration(@Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("password") String password, @Field("firebaseToken") String firebaseToken);

    @FormUrlEncoded
    @POST("login.php")
    Call<User> doLogin(@Field("mobile") String mobile, @Field("password") String password, @Field("firebaseToken") String firebaseToken);


    @FormUrlEncoded
    @POST("token_verification.php")
    Call<User> doTokenVerification(@Field("mobile") String mobile, @Field("verificationToken") int verificationToken);


    @FormUrlEncoded
    @POST("update_firebase_token.php")
    Call<User> doFirebaseTokenUpdate(@Field("mobile") String mobile, @Field("firebaseToken") String firebaseToken);


    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<DriverServerResponse> doSendPasswordViaSMS(@Field("mobile") String mobile);



    @Multipart
    @POST("upload_driver_reg_step1.php")
    Call <DriverServerResponse> doDriverRegistrationStep1(@Part MultipartBody.Part picture, @Part MultipartBody.Part licence,
                                              @Part MultipartBody.Part cnic_front,@Part MultipartBody.Part cnic_rear,@Part("cnic") String cnic,
                                              @Part("name") String name,@Part("father") String father,@Part("mobile") String mobile);


    @Multipart
    @POST("upload_driver_reg_step2.php")
    Call <DriverServerResponse> doDriverRegistrationStep2(@Part MultipartBody.Part vehicle_front, @Part MultipartBody.Part vehicle_rear,
                                                          @Part MultipartBody.Part registration,@Part MultipartBody.Part route,@Part("reg_alphabet") String reg_alphabet,
                                                          @Part("reg_year") String reg_year,@Part("reg_no") String reg_no,@Part("mobile") String mobile);

}
