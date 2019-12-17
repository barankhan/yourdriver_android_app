package com.baran.driver.Fragments.driver.home;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Activity.RideAlertActivity;
import com.baran.driver.BuildConfig;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.DriverAlertReceiver;
import com.baran.driver.Services.LocationBackgroundService;
import com.baran.driver.Services.LocationServiceCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import android.widget.Button;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.core.content.ContextCompat.getMainExecutor;


public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationServiceCallback{


    public LocationBackgroundService gpsService;

    private final int RIDE_ALERT_REQUEST_CODE = 10001;

    private static final String TAG = MainActivity.class.getSimpleName();

    CameraPosition cameraPosition ;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    private Boolean mRequestingLocationUpdates;


    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;


    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_PICKUP_ZOOM = 18;
    private final LatLng mDefaultLocation = new LatLng(30.2223, 71.4703);
    private LocationCallback locationCallback;
    private Marker currentLocationMarker;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    Fragment mapFrag;
    private String provider;
    SupportMapFragment mapFragment;

    private LocationManager locationManager;

    private String mLastUpdateTime;
    private User currentUser;
    private Button btnOnOffLine;
    Intent serviceIntent;
    DriverAlertReceiver receiver;
    IntentFilter intentFilter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.driver_fragment_home, container, false);

//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        View frg = root.findViewById(R.id.driver_map);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.driver_map);


        btnOnOffLine = root.findViewById(R.id.btn_on_off_line);
        btnOnOffLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlineOffLineButtonClicked();
            }
        });


        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";

        currentUser = MainActivity.appPreference.getUserObject(getContext(),getActivity());
        buildLocationSettingsRequest();

        serviceIntent = new Intent(getActivity().getApplication(), LocationBackgroundService.class);
        getActivity().getApplication().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        createNotificationChannel();


        receiver = new DriverAlertReceiver();
        intentFilter = new IntentFilter("com.barankhan.driver.ride_alerts");
        getContext().registerReceiver(receiver, intentFilter);

        return root;
    }


    private void onlineOffLineButtonClicked(){
        String status= btnOnOffLine.getTag().toString();
        if(status.equals("Offline")){
            Call<DriverServerResponse> userCall = MainActivity.serviceApi.isDriverOnline(currentUser.getMobile(),1);
            userCall.enqueue(new Callback<DriverServerResponse>() {
                @Override
                public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                    if(response.isSuccessful()){

                        currentUser.setIsDriverOnline(1);
                        btnOnOffLine.setTag(response.body().getMessage());
                        btnOnOffLine.setText(response.body().getMessage());
                        MainActivity.appPreference.setUserObject(currentUser);
                        btnOnOffLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.online));
                        gpsService.startTracking();
                    }else{
                        Utils.showAlertBox(getActivity(),"Sorry! Please try again later!");
                    }
                }

                @Override
                public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                    Utils.showAlertBox(getActivity(),"Something went wrong! Please try again later.");
                }
            });
        }else{
            Call<DriverServerResponse> userCall = MainActivity.serviceApi.isDriverOnline(currentUser.getMobile(),0);
            userCall.enqueue(new Callback<DriverServerResponse>() {
                @Override
                public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                    if(response.isSuccessful()){
                        currentUser.setIsDriverOnline(0);
                        btnOnOffLine.setTag(response.body().getMessage());
                        btnOnOffLine.setText(response.body().getMessage());
                        MainActivity.appPreference.setUserObject(currentUser);
                        gpsService.stopTracking();
                        btnOnOffLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.offline));
                    }else{
                        Utils.showAlertBox(getActivity(),"Sorry! Please try again later!");
                    }
                }

                @Override
                public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                    Utils.showAlertBox(getActivity(),"Something went wrong! Please try again later.");
                }
            });
        }

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            String name = className.getClassName();
            if (name.endsWith("LocationBackgroundService")) {
                gpsService = ((LocationBackgroundService.LocationServiceBinder) service).getService();
                gpsService.registerCallBack(HomeFragment.this);

            }
        }

        public void onServiceDisconnected(ComponentName className) {
            if (className.getClassName().equals("LocationBackgroundService")) {
                gpsService = null;
            }
        }
    };


    @Override
    public void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((mRideAlertReceiver),
                new IntentFilter("RideAlert")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRideAlertReceiver);
    }

    private BroadcastReceiver mRideAlertReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("RideAlertBraodCaster","i'm here");
            Intent i = new Intent(getContext(), RideAlertActivity.class);
            i.putExtras(intent.getExtras());
            startActivityForResult(i, RIDE_ALERT_REQUEST_CODE);
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        if (isLocationEnabled()) {
            mapFragment.getMapAsync(this);
            if (mRequestingLocationUpdates && checkPermissions()) {

            } else if (!checkPermissions()) {
                requestPermissions();
            }



        }else{
            buildAlertMessageNoGps();
        }

    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");

                }
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                Log.e("permission","fucked");
            }
        }
    }



    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, Please enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        getLocationPermission();
        updateLocationUI();


    }



    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    /**
     * Handles the result of the request for location permissions.
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        mLocationPermissionGranted = false;
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    mLocationPermissionGranted = true;
//                }
//            }
//        }
//        updateLocationUI();
//    }




    private void updateLocationOnServer(Location currentLocation){
        if (currentLocation != null) {

            MainActivity.appPreference.showToast(String.valueOf(currentLocation.getLatitude())+"=="+String.valueOf(currentLocation.getLongitude()));
            MainActivity.appPreference.setLat(String.valueOf(currentLocation.getLatitude()));
            MainActivity.appPreference.setLng(String.valueOf(currentLocation.getLongitude()));


            Ride r ;
            r = MainActivity.appPreference.getRideObject();
            String ride_id="";
            String passenger_id="";
            if(r!=null){
                ride_id=String.valueOf(r.getId());
                passenger_id = String.valueOf(r.getPassengerId()) ;
            }


            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(round(currentLocation.getLatitude(),6),round(currentLocation.getLongitude(),6))).zoom(DEFAULT_PICKUP_ZOOM).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            Call<DriverServerResponse> userCall = MainActivity.serviceApi.doUpdateLatLong(currentUser.getMobile(),currentLocation.getLatitude(),currentLocation.getLongitude(),ride_id,passenger_id);
            userCall.enqueue(new Callback<DriverServerResponse>() {
                @Override
                public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {

                }

                @Override
                public void onFailure(Call<DriverServerResponse> call, Throwable t) {

                }
            });
        }

    }

    public double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }


    private void updateLocationUI() {

        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }



    @Override
    public void getResult(Location result) {
        updateLocationOnServer(result);

    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Passenger Alert";
            String description = "You will get alert when a Passenger will request for a ride";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("121212", name, importance);
            channel.setDescription(description);
            Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.ride_alert);
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            channel.setSound(uri,att);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}