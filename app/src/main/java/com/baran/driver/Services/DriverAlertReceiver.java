package com.baran.driver.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baran.driver.Activity.RideAlertActivity;

public class DriverAlertReceiver extends BroadcastReceiver {
    public DriverAlertReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
            Log.e("Alert Recieved","here we go");
            Intent i = new Intent(context, RideAlertActivity.class);
            i.putExtras(intent);
            context.startActivity(i);
    }
}
