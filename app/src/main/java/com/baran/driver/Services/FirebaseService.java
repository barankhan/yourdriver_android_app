package com.baran.driver.Services;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Constants.Constant;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.baran.driver.Services.ServiceApi;
import com.baran.driver.Services.RetrofitClient;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FirebaseService extends FirebaseMessagingService {
    private LocalBroadcastManager broadcaster;
    public RidesApi ridesApi;


    String TAG = this.getClass().toString();

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
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
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG,remoteMessage.getMessageId());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            if(remoteMessage.getData().get("key").equals("driver_successful")){
                MainActivity.appPreference.setLoginStatus(false);
                MainActivity.appPreference.setUserObject(null);
                Intent startMain = new Intent(getBaseContext(), MainActivity.class);
                startMain.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }else if(remoteMessage.getData().get("key").equals("ride_alert")){
                Intent intent = new Intent();
                intent.putExtra("phone", remoteMessage.getData().get("msg"));
                intent.putExtra("lat", remoteMessage.getData().get("lat"));
                intent.putExtra("lng", remoteMessage.getData().get("lng"));
                intent.putExtra("ride_id", remoteMessage.getData().get("ride_id"));
                Log.e(TAG,"Boardcast Sent");
                intent.setAction("com.barankhan.driver.ride_alerts");
                sendBroadcast(intent);
                User u = MainActivity.appPreference.getUserObjectWithoutUserValidation();
                Call<DriverServerResponse> rideCall = ridesApi.rideAlertReceived(u.getMobile(),remoteMessage.getData().get("ride_id"),remoteMessage.getMessageId());
                rideCall.enqueue(new Callback<DriverServerResponse>() {
                    @Override
                    public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<DriverServerResponse> call, Throwable t) {

                    }
                });
            }
            else if(remoteMessage.getData().get("key").equals("ride_accepted")){
                Intent intent = new Intent("RideAccepted");
                intent.putExtra("phone", remoteMessage.getData().get("msg"));
                intent.putExtra("lat", remoteMessage.getData().get("lat"));
                intent.putExtra("lng", remoteMessage.getData().get("lng"));
                intent.putExtra("driver_mobile", remoteMessage.getData().get("driver_mobile"));
                broadcaster.sendBroadcast(intent);
            }else if(remoteMessage.getData().get("key").equals("driver_location_update")){
                Intent intent = new Intent("DriverLocationUpdate");
                intent.putExtra("lat", remoteMessage.getData().get("lat"));
                intent.putExtra("lng", remoteMessage.getData().get("lng"));
                broadcaster.sendBroadcast(intent);
            }
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
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
        User u =  MainActivity.appPreference.getUserObjectWithoutUserValidation();
        if(u!=null){
            Call<User> userCall = MainActivity.serviceApi.doFirebaseTokenUpdate(u.getMobile(), token);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.e("TokenUpdate","OnNewToken Update failed");
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("TokenUpdate","OnNewToken Update failed");
                }
            });
        }
    }


}
