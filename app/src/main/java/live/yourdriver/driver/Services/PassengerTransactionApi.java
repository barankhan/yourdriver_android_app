package live.yourdriver.driver.Services;

import live.yourdriver.driver.Model.DriverTransaction;
import live.yourdriver.driver.Model.UserRideTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PassengerTransactionApi {

    @FormUrlEncoded
    @POST("passenger_transactions.php")
    Call <List<DriverTransaction>> getTransactions(@Field("passenger_id") int PassengerID, @Field("page_no") int pageNo);

    @FormUrlEncoded
    @POST("passenger_transaction_details.php")
    Call <UserRideTransaction> getTransactionDetails(@Field("trans_id") int transId);



}
