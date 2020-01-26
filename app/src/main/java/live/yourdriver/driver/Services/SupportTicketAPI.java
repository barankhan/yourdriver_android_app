package live.yourdriver.driver.Services;

import live.yourdriver.driver.Model.DriverServerResponse;
import live.yourdriver.driver.Model.SupportTicket;
import live.yourdriver.driver.Model.SupportTicketMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SupportTicketAPI {


    @FormUrlEncoded
    @POST("tickets.php")
    Call<List<SupportTicket>> getSupportTickets(@Field("user_id") int   UserId, @Field("page_no") int pageNo);



    @FormUrlEncoded
    @POST("new_ticket.php")
    Call<DriverServerResponse> createSupportTicket(@Field("user_id") int   UserId, @Field("message") String message, @Field("title") String title);


    @FormUrlEncoded
    @POST("insert_ticket_message.php")
    Call<DriverServerResponse> createSupportTicketMessage(@Field("user_id") int   UserId, @Field("message") String message, @Field("ticket_id") int ticketId);


    @FormUrlEncoded
    @POST("get_support_ticket_messages.php")
    Call <List<SupportTicketMessage>> getSupportTicketMessages(@Field("user_id") int userId, @Field("ticket_id") int ticketId);


}
