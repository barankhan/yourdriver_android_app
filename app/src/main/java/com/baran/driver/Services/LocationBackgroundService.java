package com.baran.driver.Services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.baran.driver.R;

import java.util.Random;

import androidx.core.app.NotificationCompat;

public class LocationBackgroundService extends Service {
    private final LocationServiceBinder binder = new LocationServiceBinder();
    private final String TAG = "BackgroundService";
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private NotificationManager notificationManager;
    private LocationServiceCallback myCallback;
    private final int LOCATION_INTERVAL = 500;
    private final int LOCATION_DISTANCE = 10;
    private static int srId=0;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void registerCallBack(LocationServiceCallback myCallback){
        this.myCallback= myCallback;
    }

    private class LocationListener implements android.location.LocationListener
    {
        private Location lastLocation = null;
        private final String TAG = "LocationListener";
        private Location mLastLocation;

        public LocationListener(String provider)
        {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            mLastLocation = location;
            myCallback.getResult(location);
//            Log.e(TAG, "LocationChanged: "+location);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
//            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
//            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
//            Log.e(TAG, "onStatusChanged: " + status);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
//        Log.i(TAG, "onCreate");

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mLocationListener);
            } catch (Exception ex) {
                Log.i(TAG, "fail to remove location listners, ignore", ex);
            }
        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void startTracking() {
        srId+=1;
        startForeground(srId, getNotification());
        initializeLocationManager();
        mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);

        try {
            mLocationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListener );

        } catch (java.lang.SecurityException ex) {
            // Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            // Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    public void stopTracking() {
        stopForeground(true);
        this.onDestroy();
    }

    private Notification getNotification() {
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(getApplicationContext(), "channel_01").setAutoCancel(true).setSmallIcon(R.drawable.ic_driver_notification_icon).setContentText("Your are Online!");
            notification = builder.build();
        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                    .setContentTitle(getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

             notification = builder.build();
        }
       return  notification;
    }


    public class LocationServiceBinder extends Binder {
        public LocationBackgroundService getService() {
            return LocationBackgroundService.this;
        }
    }
}
