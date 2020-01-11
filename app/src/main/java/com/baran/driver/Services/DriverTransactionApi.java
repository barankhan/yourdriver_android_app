package com.baran.driver.Services;

import com.baran.driver.Model.DriverTransaction;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.User;
import com.baran.driver.Model.UserRideTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DriverTransactionApi {

    @FormUrlEncoded
    @POST("driver_transactions.php")
    Call <List<DriverTransaction>> getTransactions(@Field("driver_id") int   DriverID, @Field("page_no") int pageNo);

    @FormUrlEncoded
    @POST("driver_transaction_details.php")
    Call <UserRideTransaction> getTransactionDetails(@Field("trans_id") int   transId);


    @FormUrlEncoded
    @POST("recharge_account.php")
    Call <User> recharge(@Field("user_id") int userId,@Field("payment_type") String transactionType,
                         @Field("transaction_id") String transactionId,@Field("amount") Double amount);

}
