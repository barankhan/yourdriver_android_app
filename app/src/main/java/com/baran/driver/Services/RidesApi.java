package com.baran.driver.Services;

import com.baran.driver.Model.ChatMessage;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.ExpectedFare;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.DriverTransaction;
import com.baran.driver.Model.User;
import com.baran.driver.Model.UserRide;
import com.baran.driver.Model.UserTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RidesApi {
    @FormUrlEncoded
    @POST("register_ride.php")
    Call <Ride> findDriver(@Field("mobile") String mobile, @Field("vehicle_type") String vehicle_type,
                           @Field("pickup_lat_lng") String pickup_lat_lng, @Field("dropoff_lat_lng") String dropoff_lat_lng, @Field("firebaseToken") String firebaseToken);
    @FormUrlEncoded
    @POST("accept_ride.php")
    Call <UserRide> acceptRide(@Field("mobile") String mobile, @Field("ride_id") String ride_id, @Field("driver_lat")String lat, @Field("driver_lng")String lng);

    @FormUrlEncoded
    @POST("reject_ride.php")
    Call <Ride> rejectRide(@Field("mobile") String mobile,@Field("ride_id") String ride_id);


    @FormUrlEncoded
    @POST("cancel_ride.php")
    Call <UserRide> cancelRide(@Field("mobile") String mobile,@Field("ride_id") String ride_id);

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
    Call <UserTransaction> updateTransaction(@Field("mobile") String mobile, @Field("trans_id") int trans_id, @Field("amount_received") String amount);


    @FormUrlEncoded
    @POST("ride_agora_call.php")
    Call <DriverServerResponse> triggerAgoraCall(@Field("from_mobile") String callerMobile,@Field("to_mobile") String receiver_mobile,@Field("ride_id") int rideId);


    @FormUrlEncoded
    @POST("insert_chat.php")
    Call <DriverServerResponse> insertChat(@Field("sender_id") int senderId,@Field("message") String message,@Field("ride_id") int rideId);


    @FormUrlEncoded
    @POST("get_chat.php")
    Call <List<ChatMessage>> getChat(@Field("user_id") int userId, @Field("ride_id") int rideId);



    @FormUrlEncoded
    @POST("ride_rating.php")
    Call <DriverServerResponse> setRating(@Field("driver_id") int driverId, @Field("ride_id") int rideId,@Field("rating") float rating);




    @FormUrlEncoded
    @POST("calculate_expected_fare.php")
    Call <ExpectedFare> calculateExpectedFare(@Field("mobile") String mobile, @Field("origin_lat") double origin_lat, @Field("origin_lng") double origin_lng
    , @Field("destination_lat") double destination_lat, @Field("destination_lng") double destination_lng
    );



}
