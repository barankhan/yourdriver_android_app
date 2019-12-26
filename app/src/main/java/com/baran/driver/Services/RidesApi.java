package com.baran.driver.Services;

import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.DriverTransaction;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RidesApi {
    @FormUrlEncoded
    @POST("register_ride.php")
    Call <Ride> findDriver(@Field("mobile") String mobile, @Field("vehicle_type") String vehicle_type,
                           @Field("pickup_lat_lng") String pickup_lat_lng, @Field("dropoff_lat_lng") String dropoff_lat_lng);
    @FormUrlEncoded
    @POST("accept_ride.php")
    Call <Ride> acceptRide(@Field("mobile") String mobile,@Field("ride_id") String ride_id,@Field("driver_lat")String lat,@Field("driver_lng")String lng);

    @FormUrlEncoded
    @POST("reject_ride.php")
    Call <Ride> rejectRide(@Field("mobile") String mobile,@Field("ride_id") String ride_id);


    @FormUrlEncoded
    @POST("cancel_ride.php")
    Call <Ride> cancelRide(@Field("mobile") String mobile,@Field("ride_id") String ride_id);

    @FormUrlEncoded
    @POST("push_notification_received.php")
    Call <DriverServerResponse> pushNotificationReceived(@Field("firebase_message_id") String firebase_message_id);


    @FormUrlEncoded
    @POST("driver_arrived.php")
    Call <Ride> driverArrived(@Field("mobile") String mobile,@Field("ride_id") int ride_id);

    @FormUrlEncoded
    @POST("start_ride.php")
    Call <Ride> startRide(@Field("mobile") String mobile,@Field("ride_id") int ride_id);

    @FormUrlEncoded
    @POST("end_ride.php")
    Call <DriverTransaction> endRide(@Field("mobile") String mobile, @Field("ride_id") int ride_id, @Field("distance") Double distance);

    @FormUrlEncoded
    @POST("transaction_amount_received.php")
    Call <DriverServerResponse> updateTransaction(@Field("mobile") String mobile,@Field("trans_id") int trans_id,@Field("amount_received") String amount);

}
