package live.yourdriver.driver.Services;

import live.yourdriver.driver.Model.DriverServerResponse;
import live.yourdriver.driver.Model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    Call <User> doDriverRegistrationStep1(@Part MultipartBody.Part picture, @Part MultipartBody.Part licence,
                                              @Part MultipartBody.Part cnic_front,@Part MultipartBody.Part cnic_rear,@Part("cnic") RequestBody cnic,
                                              @Part("name") RequestBody name,@Part("father") RequestBody father,@Part("mobile") RequestBody mobile);


    @Multipart
    @POST("upload_driver_reg_step2.php")
    Call <User> doDriverRegistrationStep2(@Part MultipartBody.Part vehicle_front, @Part MultipartBody.Part vehicle_rear,
                                                          @Part MultipartBody.Part registration,@Part MultipartBody.Part route,@Part("reg_alphabet") RequestBody reg_alphabet,
                                                          @Part("reg_year") RequestBody reg_year,@Part("reg_no") RequestBody reg_no,@Part("mobile") RequestBody mobile
            ,@Part("vehicle_type") RequestBody vehicle_type);


    @FormUrlEncoded
    @POST("update_lat_long.php")
    Call <DriverServerResponse> doUpdateLatLong(@Field("mobile") String mobile, @Field("lat") Double lat, @Field("lng") Double lng,@Field("ride_id") String ride_id,@Field("passenger_id") String passenger_id);


    @FormUrlEncoded
    @POST("is_driver_online.php")
    Call <User> isDriverOnline(@Field("mobile") String mobile, @Field("is_driver_online") int is_driver_online, @Field("firebaseToken") String firebaseToken);


    @FormUrlEncoded
    @POST("find_driver.php")
    Call <DriverServerResponse> findDriver(@Field("mobile") String mobile, @Field("lat") double lat,
                                           @Field("lng") double lng,@Field("rejected_ids") String rejected_ids);


    @FormUrlEncoded
    @POST("update_profile.php")
    Call<User> updateProfile(@Field("mobile") String mobile,@Field("name") String name, @Field("father") String father,@Field("email") String email);



    @Multipart
    @POST("update_profile_picture.php")
    Call <User> updateProfilePicture(@Part MultipartBody.Part picture,@Part("mobile") RequestBody mobile);



    @FormUrlEncoded
    @POST("resend_otp.php")
    Call<User> resendOTP(@Field("mobile") String mobile);


    @FormUrlEncoded
    @POST("update_password.php")
    Call<User> updatePassword(@Field("mobile") String mobile,@Field("old_password") String oldPassword,@Field("new_password") String newPassword);


}
