package live.yourdriver.driver.Services;

import android.content.Intent;
import android.util.Log;

import live.yourdriver.driver.Activity.ChatActivity;
import live.yourdriver.driver.Activity.NotifActivity;
import live.yourdriver.driver.Activity.RideAlertActivity;
import live.yourdriver.driver.Activity.VoiceChatViewActivity;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Model.DriverServerResponse;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;
    public RidesApi ridesApi;
    public AppPreference appPreference;


    String TAG = this.getClass().toString();

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
        appPreference = new AppPreference(this);

    }

    public FirebaseService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
//        final MediaPlayer mp = MediaPlayer.create(this, R.raw.ride_alert);
//        mp.start();
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG,"Received messaage .........");

        Call<DriverServerResponse> rideCall = ridesApi.pushNotificationReceived(remoteMessage.getMessageId());
        rideCall.enqueue(new Callback<DriverServerResponse>() {
            @Override
            public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {

            }

            @Override
            public void onFailure(Call<DriverServerResponse> call, Throwable t) {

            }
        });


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            if(remoteMessage.getData().get("key").equals("driver_successful")){
                appPreference.setLoginStatus(false);
                appPreference.setUserObject(null);
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.putExtra("goToLogin","yes");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);



            }else if(remoteMessage.getData().get("key").equals("ride_alert")){
                Intent intent = new Intent(this, RideAlertActivity.class);
                intent.putExtra("phone", remoteMessage.getData().get("msg"));
                intent.putExtra("lat", remoteMessage.getData().get("lat"));
                intent.putExtra("lng", remoteMessage.getData().get("lng"));
                intent.putExtra("ride_id", remoteMessage.getData().get("ride_id"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(remoteMessage.getData().get("key").equals("p_amount_received")){
                appPreference.setUserObjectWithEncodedJson(remoteMessage.getData().get("user"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else if(remoteMessage.getData().get("key").equals("p_ride_accepted")){
                appPreference.setDriverObjectWithEncodedJson(remoteMessage.getData().get("driver"));
                appPreference.setRideObjectWithEncodedJson(remoteMessage.getData().get("ride"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("d_ride_cancelled")){
                appPreference.setRideObject(null);
                appPreference.setUserObjectWithEncodedJson(remoteMessage.getData().get("user"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("p_ride_cancelled")){
                appPreference.setDriverObject(null);
                appPreference.setRideObject(null);

                appPreference.setPassengerObject(null);
                appPreference.setUserObjectWithEncodedJson(remoteMessage.getData().get("user"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.putExtra("setPickUpMode", "true");
                intent.putExtra("setDropoffMode", "false");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("p_driver_arrived")){
                appPreference.setRideObjectWithEncodedJson(remoteMessage.getData().get("ride"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("p_ride_started")){
                appPreference.setRideObjectWithEncodedJson(remoteMessage.getData().get("ride"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("p_ride_ended")){
                appPreference.setRideObjectWithEncodedJson(remoteMessage.getData().get("ride"));

                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("driver_location_update")){
                Intent intent = new Intent("DriverLocationUpdate");
                intent.putExtra("lat", remoteMessage.getData().get("lat"));
                intent.putExtra("lng", remoteMessage.getData().get("lng"));
                broadcaster.sendBroadcast(intent);
            }else if(remoteMessage.getData().get("key").equals("call_alert")){
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message", remoteMessage.getData().get("message"));
                intent.putExtra("agora_channel", remoteMessage.getData().get("agora_channel"));
                intent.putExtra("ride_id", remoteMessage.getData().get("ride_id"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("chat_message_received")){
                if(ChatActivity.inFront){
                    Intent intent = new Intent("NewChatMessageReceived");
                    intent.putExtra("message", remoteMessage.getData().get("message"));
                    intent.putExtra("sender_id", remoteMessage.getData().get("sender_id"));
                    broadcaster.sendBroadcast(intent);
                }else{
                    Intent intent = new Intent(this, ChatActivity.class);
                    intent.putExtra("ride_id",remoteMessage.getData().get("ride_id"));
                    intent.putExtra("new_message_received","aahoo");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else if(remoteMessage.getData().get("key").equals("ticket_updated")){
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message",remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("teasing")){
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message",remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("call_rejected")){
                if(VoiceChatViewActivity.inFront){
                    Intent intent = new Intent("NewCallRejectedReceived");
                    broadcaster.sendBroadcast(intent);
                }else{
                    Intent intent = new Intent(this, NotifActivity.class);
                    intent.putExtra("message","Call Rejected");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else if(remoteMessage.getData().get("key").equals("offline_driver")){
                appPreference.setUserObjectWithEncodedJson(remoteMessage.getData().get("user"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message",remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(remoteMessage.getData().get("key").equals("reset_to_default")){
                appPreference.setDriverObject(null);
                appPreference.setRideObject(null);
                appPreference.setPassengerObject(null);
                appPreference.setUserObjectWithEncodedJson(remoteMessage.getData().get("user"));
                Intent intent = new Intent(this, NotifActivity.class);
                intent.putExtra("message",remoteMessage.getData().get("message"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

//            Log.e(TAG, "ChatMessage data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

//            Log.e(TAG, "ChatMessage Notification Body: " + remoteMessage.getNotification().getBody());
        }





        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {

    }


}
