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

import com.baran.driver.Activity.DriverActivity;
import com.baran.driver.Activity.DriverTransactionActivity;
import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Activity.RideAlertActivity;
import com.baran.driver.BuildConfig;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.DBHelper;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.RidePath;
import com.baran.driver.Model.DriverTransaction;
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
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

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


public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationServiceCallback, View.OnClickListener, GoogleMap.OnCameraMoveListener {


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
    private static float DEFAULT_PICKUP_ZOOM = 18;
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
    Button btnDriverArrived,btnCancelRide,btnStartRide,btnEndRide;
    private Marker pickUpMarker=null, dropOffMarker=null;
    private DBHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.driver_fragment_home, container, false);

//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        View frg = root.findViewById(R.id.driver_map);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.driver_map);

        btnDriverArrived = root.findViewById(R.id.btn_driver_arrived);
        btnCancelRide = root.findViewById(R.id.btn_driver_ride_cancel);
        btnStartRide = root.findViewById(R.id.btn_start_ride);
        btnEndRide = root.findViewById(R.id.btn_end_ride);

        btnDriverArrived.setOnClickListener(this);
        btnCancelRide.setOnClickListener(this);
        btnStartRide.setOnClickListener(this);
        btnEndRide.setOnClickListener(this);

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

        Log.e("here we resume","sure");

        Ride r = MainActivity.appPreference.getRideObject();
        if(r!=null) {
            Location currentLocation = new Location("right now");
            currentLocation.setLatitude(Double.valueOf(MainActivity.appPreference.getLat()));
            currentLocation.setLongitude(Double.valueOf(MainActivity.appPreference.getLng()));
            showArrivedButtons(r, currentLocation);
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

        dropOffMarker = mMap.addMarker(new MarkerOptions()
                .position(mDefaultLocation)
                .draggable(true).icon(Utils.getBitmapFromVector(getContext(), R.drawable.ic_drop_off_locatin_marker)));
        dropOffMarker.setVisible(false);


        pickUpMarker = mMap.addMarker(new MarkerOptions()
                .position(mDefaultLocation)
                .draggable(true).icon(Utils.getBitmapFromVector(getContext(), R.drawable.ic_locatin_marker)));
        pickUpMarker.setVisible(false);
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
                showArrivedButtons(r,currentLocation);
                if (r.getIsRideStarted()==1){
                    dbHelper = new DBHelper(getContext());
                    dbHelper.insertRidePath(currentLocation.getLatitude(),currentLocation.getLongitude(),r.getId());
                }
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


    private void showArrivedButtons(Ride r,Location currentLocation){

        Location startPoint=new Location("pickup_location");
        startPoint.setLatitude(r.getPickupLat());
        startPoint.setLongitude(r.getPickupLng());
        double distance=startPoint.distanceTo(currentLocation);
        Log.e("distance",String.valueOf(distance));
        if(distance<=50 && r.getIsRideStarted()==0){
            btnDriverArrived.setVisibility(View.VISIBLE);
            btnCancelRide.setVisibility(View.VISIBLE);
        }else{
            btnDriverArrived.setVisibility(View.GONE);
            btnCancelRide.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        User u = MainActivity.appPreference.getUserObject(getContext(),getActivity());
        Ride r = MainActivity.appPreference.getRideObject();
        switch (v.getId()){
            case R.id.btn_driver_arrived:

                Call<Ride> userCall = DriverActivity.ridesApi.driverArrived(u.getMobile(),r.getId());
                Utils.showProgressBarSpinner(getContext());
                userCall.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        Utils.dismissProgressBarSpinner();
                        if(response.isSuccessful()){
                            MainActivity.appPreference.setRideObject(response.body());
                            btnDriverArrived.setVisibility(View.GONE);
                            btnStartRide.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                    }
                });
                break;
            case R.id.btn_driver_ride_cancel:
                Call<Ride> rideCall = DriverActivity.ridesApi.cancelRide(currentUser.getMobile(),String.valueOf(r.getId()));
                Utils.showProgressBarSpinner(getContext());
                rideCall.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        MainActivity.appPreference.setRideObject(response.body());
                        Utils.dismissProgressBarSpinner();
                        if(response.isSuccessful()){
                            if(response.body().getResponse().equals("ride_cancelled_successfully")){
                                btnStartRide.setVisibility(View.GONE);
                                btnCancelRide.setVisibility(View.GONE);
                                btnEndRide.setVisibility(View.GONE);
                                btnDriverArrived.setVisibility(View.GONE);
                                Utils.showAlertBox(getActivity(),"Ride Cancelled Successfully");
                            }else if(response.body().getResponse().equals("ride_cancel_error") && response.body().getIsRideCancelled()==1){
                                btnStartRide.setVisibility(View.GONE);
                                btnCancelRide.setVisibility(View.GONE);
                                btnEndRide.setVisibility(View.GONE);
                                btnDriverArrived.setVisibility(View.GONE);
                                Utils.showAlertBox(getActivity(),"Ride Cancelled Successfully");
                            }else{
                                Utils.showAlertBox(getActivity(),"Unable to cancel Ride");
                            }
                        }else{
                            Utils.showAlertBox(getActivity(),"Something went wrong. Please try again!");
                        }
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                        Utils.showAlertBox(getActivity(),"Unable to communicate with server.");
                    }
                });
                break;

            case R.id.btn_start_ride:
                Call<Ride> startRideCall = DriverActivity.ridesApi.startRide(u.getMobile(),r.getId());
                Utils.showProgressBarSpinner(getContext());
                startRideCall.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {
                        if(response.isSuccessful()){

                            MainActivity.appPreference.setRideObject(response.body());
                            btnStartRide.setVisibility(View.GONE);
                            btnCancelRide.setVisibility(View.GONE);
                            btnDriverArrived.setVisibility(View.GONE);
                            btnEndRide.setVisibility(View.VISIBLE);
                            dbHelper = new DBHelper(getContext());
                            dbHelper.insertRidePath(Double.valueOf(MainActivity.appPreference.getLat()),Double.valueOf(MainActivity.appPreference.getLng()),response.body().getId());
                            Utils.dismissProgressBarSpinner();
                        }
                        Utils.dismissProgressBarSpinner();
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                    }
                });
                break;
            case R.id.btn_end_ride:
                Utils.showProgressBarSpinner(getContext());
                // Loop through all the saved paths and update them.
                List<RidePath> ridePathList = dbHelper.getRidePath(r.getId());

                Location startPoint=new Location("first_mark");
                Location endPoint=new Location("second_mark");



                double distance=0;
                for (int i=1;i<ridePathList.size();i++) {
                    RidePath p1 = ridePathList.get(i-1);
                    RidePath p2 = ridePathList.get(i);
                    startPoint.setLatitude(p1.getLat());
                    startPoint.setLongitude(p1.getLng());
                    endPoint.setLatitude(p2.getLat());
                    endPoint.setLongitude(p2.getLng());
                    distance += startPoint.distanceTo(endPoint);
                }

                Call<DriverTransaction> endRideCall = DriverActivity.ridesApi.endRide(u.getMobile(),r.getId(),distance);

                endRideCall.enqueue(new Callback<DriverTransaction>() {
                    @Override
                    public void onResponse(Call<DriverTransaction> call, Response<DriverTransaction> response) {

                        if(response.isSuccessful()){
                            MainActivity.appPreference.setDriverTransactionObject(response.body());
                            MainActivity.appPreference.setRideObject(null);
                            btnStartRide.setVisibility(View.GONE);
                            btnCancelRide.setVisibility(View.GONE);
                            btnEndRide.setVisibility(View.GONE);
                            btnDriverArrived.setVisibility(View.GONE);
                            Intent intent = new Intent(getContext(), DriverTransactionActivity.class);
                            startActivity(intent);
                            Utils.dismissProgressBarSpinner();
                            Log.e("i got in full success",response.body().getResponse());
                        }else{
                            Log.e("i got in half success",response.body().getResponse());
                            Utils.dismissProgressBarSpinner();
                        }

                    }

                    @Override
                    public void onFailure(Call<DriverTransaction> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                        Log.e("i got in failure",t.toString());
                    }
                });
                break;

        }
    }

    @Override
    public void onCameraMove() {
        DEFAULT_PICKUP_ZOOM = mMap.getCameraPosition().zoom;
    }
}