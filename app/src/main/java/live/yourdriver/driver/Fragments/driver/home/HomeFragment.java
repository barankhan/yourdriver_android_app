package live.yourdriver.driver.Fragments.driver.home;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import live.yourdriver.driver.Activity.ChatActivity;
import live.yourdriver.driver.Activity.DriverActivity;
import live.yourdriver.driver.Activity.DriverTransactionActivity;
import live.yourdriver.driver.Activity.VoiceChatViewActivity;
//import live.yourdriver.driver.BuildConfig;
import live.yourdriver.driver.BuildConfig;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.DBHelper;
import live.yourdriver.driver.Model.DriverServerResponse;
import live.yourdriver.driver.Model.Ride;
import live.yourdriver.driver.Model.RidePath;
import live.yourdriver.driver.Model.DriverTransaction;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.Model.UserRide;


import live.yourdriver.driver.Services.ChatHeadService;
import live.yourdriver.driver.Services.LocationBackgroundService;
import live.yourdriver.driver.Services.LocationServiceCallback;

import live.yourdriver.driver.Services.RetrofitClient;


import live.yourdriver.driver.R;;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import live.yourdriver.driver.Services.ServiceApi;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.checkSelfPermission;


public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationServiceCallback, View.OnClickListener, GoogleMap.OnCameraMoveListener {


    public LocationBackgroundService gpsService;

    private final int RIDE_ALERT_REQUEST_CODE = 10001;

    private static final String TAG = DriverActivity.class.getSimpleName();

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
    IntentFilter intentFilter,intentFilter1;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    Button btnDriverArrived,btnCancelRide,btnStartRide,btnEndRide,btnNavigation;
    private Marker pickUpMarker=null, dropOffMarker=null;

    private NavigationView mNavigationView;
    private View headerView;
    private Picasso picasso=null;
    private static boolean initializedPicasso = false;
    private ImageView imCallPassenger,imDriverAvatar,imMessagePassenger;
    private TextView tvPassengerName;
    private LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
    private String firebaseToken;

    public static AppPreference appPreference;

    public static ServiceApi serviceApi;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.driver_fragment_home, container, false);

//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON

        );

        appPreference = new AppPreference(getContext());
        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_USERS_API).create(ServiceApi.class);
//
         mNavigationView = getActivity().findViewById(R.id.d_nav_view);
        headerView = mNavigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.tv_driver_name);
        userName.setText(appPreference.getDisplayName());
        TextView userEmail = headerView.findViewById(R.id.tv_driver_email);
        userEmail.setText(appPreference.getDisplayEmail());




        imDriverAvatar = headerView.findViewById(R.id.im_driver_pic_hd);


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.driver_map);

        imCallPassenger = root.findViewById(R.id.im_call_passenger);
        tvPassengerName = root.findViewById(R.id.tv_passenger_name);
        imMessagePassenger = root.findViewById(R.id.im_message_passenger);

        btnDriverArrived = root.findViewById(R.id.btn_driver_arrived);
        btnCancelRide = root.findViewById(R.id.btn_driver_ride_cancel);
        btnStartRide = root.findViewById(R.id.btn_start_ride);
        btnEndRide = root.findViewById(R.id.btn_end_ride);
        btnNavigation = root.findViewById(R.id.btn_navigation);

        btnDriverArrived.setOnClickListener(this);
        btnCancelRide.setOnClickListener(this);
        btnStartRide.setOnClickListener(this);
        btnEndRide.setOnClickListener(this);
        btnNavigation.setOnClickListener(this);

        imCallPassenger.setOnClickListener(this);
        imMessagePassenger.setOnClickListener(this);

        btnOnOffLine = root.findViewById(R.id.btn_on_off_line);
        btnOnOffLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlineOffLineButtonClicked();
            }
        });


        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";


        buildLocationSettingsRequest();

        serviceIntent = new Intent(getActivity().getApplication(), LocationBackgroundService.class);
        getActivity().getApplication().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        createNotificationChannel();


        currentUser = appPreference.getUserObject(getContext(),getActivity());
        if(currentUser.getPicture()!="") {
            try
            {   Picasso.Builder picassoBuilder = new Picasso.Builder(getContext());
                picassoBuilder.downloader(new OkHttp3Downloader(RetrofitClient.okClient()));
                picasso = picassoBuilder.build();
                Picasso.setSingletonInstance(picasso); //apply to default singleton instance
            }
            catch ( IllegalStateException e )
            {}
            picasso.get().load(Constant.baseUrl.UPLOADS_URL+currentUser.getPicture()).noPlaceholder().fit().centerCrop()
                    .into(imDriverAvatar);

        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getContext().getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }else{

        }

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.FOREGROUND_SERVICE,
                android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.ACCESS_NETWORK_STATE,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                android.Manifest.permission.WAKE_LOCK
        };

        if (!Utils.hasPermissions(getContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }



        return root;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    private void onlineOffLineButtonClicked(){
        String status= btnOnOffLine.getTag().toString();

        if(status.equals("Offline")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AlertDialog d =builder.setTitle("You want to be Online")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utils.showProgressBarSpinner(getContext());
                            Call<User> userCall = serviceApi.isDriverOnline(currentUser.getMobile(),1,firebaseToken);

                            userCall.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    Utils.dismissProgressBarSpinner();
                                    if(response.isSuccessful()){
                                        appPreference.setUserObject(response.body());
                                        currentUser = response.body();
                                        if(currentUser.getIsDriverOnline()==1){
                                            btnOnOffLine.setTag(response.body().getMessage());
                                            btnOnOffLine.setText(response.body().getMessage());
                                            btnOnOffLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.online));
                                            gpsService.startTracking();

                                        }else{
                                            Utils.showAlertBox(getActivity(),currentUser.getMessage());
                                        }
                                    }else{
                                        Utils.showAlertBox(getActivity(),"Sorry! Please try again later!");
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Utils.showAlertBox(getActivity(),"Something went wrong! Please try again later.");
                                }
                            });
                        }
                    }).setNegativeButton(R.string.no,null).create();
            d.show();




        }else{




            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AlertDialog d =builder.setTitle("You want to go Offline").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Utils.showProgressBarSpinner(getContext());
                    Call<User> userCall = serviceApi.isDriverOnline(currentUser.getMobile(),0,firebaseToken);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Utils.dismissProgressBarSpinner();
                            if(response.isSuccessful()){
                                appPreference.setUserObject(response.body());
                                currentUser = response.body();
                                if(response.body().getResponse().equals("success")){
                                    btnOnOffLine.setTag(response.body().getMessage());
                                    btnOnOffLine.setText(response.body().getMessage());
                                    gpsService.stopTracking();
                                    btnOnOffLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.offline));
                                }else{
                                    Utils.showAlertBox(getActivity(),response.body().getMessage());
                                }

                            }else{
                                Utils.showAlertBox(getActivity(),"Sorry! Please try again later!");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Utils.showAlertBox(getActivity(),"Something went wrong! Please try again later.");
                        }
                    });
                }
            }).setNegativeButton(R.string.cancel,null).create();

            d.show();





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

    }

    @Override
    public void onStop() {
        super.onStop();

    }



    @Override
    public void onResume(){
        super.onResume();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                firebaseToken = mToken;

            }
        });


        if (isLocationEnabled()) {
            mapFragment.getMapAsync(this);
            if (mRequestingLocationUpdates && checkPermissions()) {

            } else if (!checkPermissions()) {
                requestPermissions();
            }

        }else{
            buildAlertMessageNoGps();
        }

        Ride r = appPreference.getRideObject();
        navigationButtonVisibility(r);
        if(r!=null) {
            if(pickUpMarker!=null) {
                pickUpMarker.setPosition(new LatLng(r.getPickupLat(), r.getPickupLng()));
                pickUpMarker.setVisible(true);
            }

            if(r.getDropoffLat()!=null && dropOffMarker!=null){
                dropOffMarker.setPosition(new LatLng(r.getDropoffLat(),r.getDropoffLng()));

            }

            Location currentLocation = new Location("right now");
            currentLocation.setLatitude(Double.valueOf(appPreference.getLat()));
            currentLocation.setLongitude(Double.valueOf(appPreference.getLng()));
            if(r.getIsRideStarted()!=1){
                btnCancelRide.setVisibility(View.VISIBLE);
            }


            User passenger = appPreference.getPassengerObject();
            if(passenger!=null){
                tvPassengerName.setText(passenger.getName());
                tvPassengerName.setVisibility(View.VISIBLE);
                imCallPassenger.setVisibility(View.VISIBLE);
                imMessagePassenger.setVisibility(View.VISIBLE);
            }

            showArrivedButtons(r, currentLocation);
            showEndRideButton(r);
            showStartRideButton(r);







        }else{
            initialState();
        }

        User u = appPreference.getUserObject(getContext(),getActivity());
        if(u!=null){
            if(u.getIsDriverOnline()==1){
                btnOnOffLine.setTag("Online");
                btnOnOffLine.setText("Online");
                btnOnOffLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.online));


            }else{
                btnOnOffLine.setTag("Offline");
                btnOnOffLine.setText("Offline");
                btnOnOffLine.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.offline));

            }
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
//                Log.e("permission","fucked");
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

        mMap.setOnCameraMoveListener(this);

        if(dropOffMarker==null) {
            dropOffMarker = mMap.addMarker(new MarkerOptions()
                    .position(mDefaultLocation)
                    .draggable(true).icon(Utils.getBitmapFromVector(getContext(), R.drawable.ic_drop_off_locatin_marker)));
            dropOffMarker.setVisible(false);
        }

        if(pickUpMarker==null){
            pickUpMarker = mMap.addMarker(new MarkerOptions()
                    .position(mDefaultLocation)
                    .draggable(true).icon(Utils.getBitmapFromVector(getContext(), R.drawable.ic_locatin_marker)));
            pickUpMarker.setVisible(false);
        }


        Ride r = appPreference.getRideObject();

        if(r!=null){
                pickUpMarker.setPosition(new LatLng(r.getPickupLat(), r.getPickupLng()));
                pickUpMarker.setVisible(true);

            if(r.getDropoffLat()!=null){
                dropOffMarker.setPosition(new LatLng(r.getDropoffLat(),r.getDropoffLng()));
//                dropOffMarker.setVisible(false);
            }
        }


        DEFAULT_PICKUP_ZOOM = 17;


//        Log.e("On Map Ready","In map Ready");

        if(appPreference.getLat()!="0" && appPreference.getLng()!="0") {
            cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.valueOf(appPreference.getLat()), Double.valueOf(appPreference.getLng()))).zoom(DEFAULT_PICKUP_ZOOM).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

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

//            appPreference.showToast(String.valueOf(currentLocation.getLatitude())+"=="+String.valueOf(currentLocation.getLongitude()));
            appPreference.setLat(String.valueOf(currentLocation.getLatitude()));
            appPreference.setLng(String.valueOf(currentLocation.getLongitude()));



            Ride r ;
            r = appPreference.getRideObject();
            String ride_id="";
            String passenger_id="";
            if(r!=null){
                ride_id=String.valueOf(r.getId());
                passenger_id = String.valueOf(r.getPassengerId()) ;
                double distance=0;
                if(r.getIsRideStarted()==0) {

                    Location startPoint = new Location("pickup_location");
                    startPoint.setLatitude(r.getPickupLat());
                    startPoint.setLongitude(r.getPickupLng());
                    distance = startPoint.distanceTo(currentLocation);
                }else if(r.getIsRideStarted()==1 && r.getDropoffLat()!=null && r.getDropoffLng()!=null){
                    Location startPoint = new Location("dropoff_location");
                    startPoint.setLatitude(r.getDropoffLat());
                    startPoint.setLongitude(r.getDropoffLng());
                    distance = startPoint.distanceTo(currentLocation);
                }
                showArrivedButtonsWithDistance(r,distance);
                if (r.getIsRideStarted()==1){
                    DBHelper dbHelper;
                    dbHelper = new DBHelper(getContext());
                    dbHelper.insertRidePath(currentLocation.getLatitude(),currentLocation.getLongitude(),r.getId());
                    dbHelper.close();
                }

                if(r.getIsRideStarted()==0){

//                    latLngBuilder.include(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).include(new LatLng(r.getPickupLat(),r.getPickupLng()));
                    //Animate to the bounds
                    cameraPosition = new CameraPosition.Builder().target(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).zoom(getZoomLevel(distance*1.5)).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else if(r.getIsRideStarted()==1 && r.getDropoffLat()!=null && r.getDropoffLng()!=null){
//                    latLngBuilder.include(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).include(new LatLng(r.getDropoffLat(),r.getDropoffLng()));
                    //Animate to the bounds
                    cameraPosition = new CameraPosition.Builder().target(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).zoom(getZoomLevel(distance*1.5)).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else{
                    cameraPosition = new CameraPosition.Builder().target(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).zoom(DEFAULT_PICKUP_ZOOM).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

            }else{
                cameraPosition = new CameraPosition.Builder().target(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude())).zoom(DEFAULT_PICKUP_ZOOM).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }



            Call<DriverServerResponse> userCall = serviceApi.doUpdateLatLong(currentUser.getMobile(),currentLocation.getLatitude(),currentLocation.getLongitude(),ride_id,passenger_id);
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


    private int getZoomLevel(double radius){
        double scale = radius / 500;
        int a =  ((int) (16 - Math.log(scale) / Math.log(2)));
        return a;
    }



    private void showArrivedButtons(Ride r,Location currentLocation){

        Location startPoint=new Location("pickup_location");
        startPoint.setLatitude(r.getPickupLat());
        startPoint.setLongitude(r.getPickupLng());
        double distance=startPoint.distanceTo(currentLocation);
        if(distance<=200 && r.getIsRideStarted()==0 && r.getIsDriverArrived()==0){
            btnDriverArrived.setVisibility(View.VISIBLE);
            if(r.getDropoffLat()!=null && dropOffMarker!=null){
                dropOffMarker.setVisible(true);
            }


        }else{
            btnDriverArrived.setVisibility(View.INVISIBLE);
        }
    }


    private void showArrivedButtonsWithDistance(Ride r,Double distance){
        if(distance<=200 && r.getIsRideStarted()==0 && r.getIsDriverArrived()==0){
            btnDriverArrived.setVisibility(View.VISIBLE);
            if(r.getDropoffLat()!=null && dropOffMarker!=null){
                dropOffMarker.setVisible(true);
            }
        }else{
            btnDriverArrived.setVisibility(View.INVISIBLE);
        }
    }


    private void showEndRideButton(Ride r){
        if(r.getIsRideStarted()==1 && r.getIsDriverArrived()==1){
            btnEndRide.setVisibility(View.VISIBLE);
            imCallPassenger.setVisibility(View.GONE);
            btnStartRide.setVisibility(View.GONE);
            btnCancelRide.setVisibility(View.GONE);
            btnDriverArrived.setVisibility(View.GONE);
            btnEndRide.setVisibility(View.VISIBLE);
            if(dropOffMarker!=null) dropOffMarker.setVisible(true);
        }
    }

    private void showStartRideButton(Ride r){
        if(r.getIsRideStarted()==0 && r.getIsDriverArrived()==1){
            btnEndRide.setVisibility(View.GONE);
            imCallPassenger.setVisibility(View.VISIBLE);
            btnStartRide.setVisibility(View.VISIBLE);
            btnCancelRide.setVisibility(View.VISIBLE);
            btnDriverArrived.setVisibility(View.INVISIBLE);
            btnEndRide.setVisibility(View.GONE);
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
//            Log.e("Exception: %s", e.getMessage());
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
//                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
//                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }

                break;

            case CODE_DRAW_OVER_OTHER_APP_PERMISSION:
                if (resultCode == Activity.RESULT_OK) {
                } else { //Permission is not available
                    Toast.makeText(getContext(),
                            "Draw over other app permission not available. Closing the application",
                            Toast.LENGTH_SHORT).show();

                }
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
        final User u = appPreference.getUserObject(getContext(),getActivity());
        final Ride r = appPreference.getRideObject();
        User p = appPreference.getPassengerObject();
        switch (v.getId()){
            case R.id.btn_driver_arrived:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                AlertDialog d =builder.setTitle("Are you arrived?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Ride> userCall = DriverActivity.ridesApi.driverArrived(u.getMobile(),r.getId());
                        Utils.showProgressBarSpinner(getContext());
                        userCall.enqueue(new Callback<Ride>() {
                            @Override
                            public void onResponse(Call<Ride> call, Response<Ride> response) {
                                Utils.dismissProgressBarSpinner();
                                if(response.isSuccessful()){
                                    if(response.body().getResponse().equals("driver_arrived")){
                                        appPreference.setRideObject(response.body());
                                        btnDriverArrived.setVisibility(View.INVISIBLE);
                                        btnStartRide.setVisibility(View.VISIBLE);
                                        navigationButtonVisibility(response.body());
                                    }else{
                                        Utils.showAlertBox(getActivity(),"Ride has been cancelled by the User!.");
                                        initialState();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<Ride> call, Throwable t) {
                                Utils.dismissProgressBarSpinner();
                            }
                        });
                    }
                }).setNegativeButton(R.string.no,null).create();

                d.show();


                break;
            case R.id.btn_driver_ride_cancel:



                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                AlertDialog d1 =builder1.setTitle("Do you want to cancel the ride?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<UserRide> rideCall = DriverActivity.ridesApi.cancelRide(currentUser.getMobile(),String.valueOf(r.getId()));
                        Utils.showProgressBarSpinner(getContext());
                        rideCall.enqueue(new Callback<UserRide>() {
                            @Override
                            public void onResponse(Call<UserRide> call, Response<UserRide> response) {
                                // When cancelling the ride; ride & passenger object should be removed from shared preference.
                                Utils.dismissProgressBarSpinner();
                                if(response.isSuccessful()){
                                    if(response.body().getResponse().equals("ride_cancelled_successfully") ) {
                                        initialState();
                                        Utils.showAlertBox(getActivity(), "Ride Cancelled Successfully");
                                        appPreference.setRideObject(null);
                                        appPreference.setPassengerObject(null);
                                        appPreference.setUserObject(response.body().getUser());

                                    }else if(response.body().getResponse().equals("ride_cancel_error") && response.body().getRide().getIsRideCancelled()==1){
                                        initialState();
                                        Utils.showAlertBox(getActivity(), "Ride Cancelled Successfully");
                                        appPreference.setRideObject(null);
                                        appPreference.setPassengerObject(null);
                                    }else{
                                        Utils.showAlertBox(getActivity(),"Unable to cancel Ride");
                                    }
                                }else{
                                    Utils.showAlertBox(getActivity(),"Something went wrong. Please try again!");
                                }
                            }

                            @Override
                            public void onFailure(Call<UserRide> call, Throwable t) {
                                Utils.dismissProgressBarSpinner();
                                Utils.showAlertBox(getActivity(),"Unable to communicate with server.");
                            }
                        });
                    }
                }).setNegativeButton(R.string.no,null).create();
                d1.show();
                break;

            case R.id.btn_start_ride:


                AlertDialog.Builder builders = new AlertDialog.Builder(getContext());
                AlertDialog ds =builders.setTitle("Do you want to start the ride?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Ride> startRideCall = DriverActivity.ridesApi.startRide(u.getMobile(),r.getId());
                        Utils.showProgressBarSpinner(getContext());
                        startRideCall.enqueue(new Callback<Ride>() {
                            @Override
                            public void onResponse(Call<Ride> call, Response<Ride> response) {
                                if(response.isSuccessful()){
                                    appPreference.setRideObject(response.body());

                                    showEndRideButton(response.body());
                                    navigationButtonVisibility(response.body());
                                    DBHelper dbHelper;
                                    dbHelper = new DBHelper(getContext());
                                    dbHelper.insertRidePath(Double.valueOf(appPreference.getLat()),Double.valueOf(appPreference.getLng()),response.body().getId());

                                    Utils.dismissProgressBarSpinner();
                                }
                                Utils.dismissProgressBarSpinner();
                            }

                            @Override
                            public void onFailure(Call<Ride> call, Throwable t) {
                                Utils.dismissProgressBarSpinner();
                            }
                        });
                    }
                }).setNegativeButton(R.string.no,null).create();

                ds.show();





                break;
            case R.id.btn_end_ride:

                AlertDialog.Builder builderc = new AlertDialog.Builder(getContext());
                AlertDialog dc =builderc.setTitle("Do you want to end the ride?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.showProgressBarSpinner(getContext());
                        // Loop through all the saved paths and update them.
                        DBHelper dbHelper = new DBHelper(getContext());;
                        List<RidePath> ridePathList = dbHelper.getRidePath(r.getId());
                        dbHelper.close();

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

                        Call<DriverTransaction> endRideCall = DriverActivity.ridesApi.endRide(u.getMobile(),r.getId(),distance,appPreference.getLat(),appPreference.getLng());

                        endRideCall.enqueue(new Callback<DriverTransaction>() {
                            @Override
                            public void onResponse(Call<DriverTransaction> call, Response<DriverTransaction> response) {

                                if(response.isSuccessful()){
                                    appPreference.setDriverTransactionObject(response.body());
                                    appPreference.setRideObject(null);
                                    appPreference.setPassengerObject(null);

                                    initialState();
                                    Intent intent = new Intent(getContext(), DriverTransactionActivity.class);
                                    startActivity(intent);
                                    Utils.dismissProgressBarSpinner();
//                            Log.e("i got in full success",response.body().getResponse());
                                }else{
//                            Log.e("i got in half success",response.errorBody().toString());
                                    Utils.dismissProgressBarSpinner();
                                }

                            }

                            @Override
                            public void onFailure(Call<DriverTransaction> call, Throwable t) {
                                Utils.dismissProgressBarSpinner();
//                        Log.e("i got in failure",t.toString());
                            }
                        });
                    }
                }).setNegativeButton(R.string.no,null).create();

                dc.show();



                break;
            case R.id.btn_navigation:

                if(r.getIsDriverArrived()==0){
                    getContext().startService(new Intent(getContext(), ChatHeadService.class));
                    Uri gmmIntentUri;
                    gmmIntentUri = Uri.parse("google.navigation:q="+r.getPickupLat()+","+r.getPickupLng()+"&mode=d");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }else if(r.getIsDriverArrived()==1 && r.getDropoffLat()>0 && r.getDropoffLng() > 0){
                    getContext().startService(new Intent(getContext(), ChatHeadService.class));
                    Uri gmmIntentUri;
                    gmmIntentUri = Uri.parse("google.navigation:q="+r.getDropoffLat()+","+r.getDropoffLng()+"&mode=d");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }


//                Log.e("notif","sending");
//                Intent intent = new Intent("com.barankhan.driver.notif_generated1");
//                intent.putExtra("message","hi");
//                getContext().sendBroadcast(intent);





                break;
            case R.id.im_call_passenger:
                if(r!=null && p!=null){
                    if(r.getPassengerId()>0 && r.getId()>0){
                        Call<DriverServerResponse> initiateCall = DriverActivity.ridesApi.triggerAgoraCall(u.getMobile(),p.getMobile(),r.getId());
                        Utils.showProgressBarSpinner(getContext());
                        initiateCall.enqueue(new Callback<DriverServerResponse>() {
                            @Override
                            public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                                Utils.dismissProgressBarSpinner();
                                if(response.isSuccessful()){
                                    Intent intent = new Intent(getContext(), VoiceChatViewActivity.class);
                                    intent.putExtra("agora_channel", "ride_call_"+r.getId());
                                    intent.putExtra("ride_id", r.getId());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                            }

                            @Override
                            public void onFailure(Call<DriverServerResponse> call, Throwable t) {

                            }
                        });
                    }
                }
                break;

            case R.id.im_message_passenger:
                if (r != null) {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("ride_id",""+r.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;

        }
    }

    @Override
    public void onCameraMove() {
//        if(mMap.getCameraPosition().zoom>10){
//            DEFAULT_PICKUP_ZOOM =mMap.getCameraPosition().zoom;
//        }


//        Log.e("In move Camera","I just got Lucky!"+DEFAULT_PICKUP_ZOOM);
    }


    private void initialState(){
        btnStartRide.setVisibility(View.GONE);
        btnCancelRide.setVisibility(View.GONE);
        btnEndRide.setVisibility(View.GONE);
        btnDriverArrived.setVisibility(View.GONE);
        imCallPassenger.setVisibility(View.GONE);
        imMessagePassenger.setVisibility(View.GONE);
        tvPassengerName.setVisibility(View.GONE);
        btnNavigation.setVisibility(View.GONE);
        if(pickUpMarker!=null) pickUpMarker.setVisible(false);
        if(dropOffMarker!=null) dropOffMarker.setVisible(false);
        navigationButtonVisibility(null);
    }



    private void navigationButtonVisibility(Ride r){
        if(r !=null){
            if(r.getIsDriverArrived()==0 || (r.getDropoffLng() !=null && r.getDropoffLat() !=null )){
                btnNavigation.setVisibility(View.VISIBLE);
            }else{
                btnNavigation.setVisibility(View.GONE);
            }
        }else{
            btnNavigation.setVisibility(View.GONE);
        }

    }
}