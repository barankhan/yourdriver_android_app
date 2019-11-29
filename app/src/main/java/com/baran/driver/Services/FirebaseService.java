package com.baran.driver.Services;

import android.util.Log;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Model.User;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseService extends FirebaseMessagingService {

    String TAG = this.getClass().toString();
    public FirebaseService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());



        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
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
        User u =  MainActivity.appPreference.getUserObject();
        if(u.getId()!=0){
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
