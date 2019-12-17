package com.baran.driver.Fragments.passenger.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Activity.Passenger;
import com.baran.driver.Activity.RideAlertActivity;
import com.baran.driver.Model.DBHelper;
import com.baran.driver.Extras.AppPreference;
import com.baran.driver.Extras.SavedLocationData;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.User;
import com.baran.driver.R;

import com.baran.driver.Activity.SearchActivity;
import com.baran.driver.adapters.DriverTypeSpinnerAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayDeque;
import java.util.Deque;


import static com.baran.driver.Extras.Utils.calculateDerivedPosition;
import static com.baran.driver.Extras.Utils.removeSavedLocationDialogue;
import static com.baran.driver.Extras.Utils.showAlertBox;
import static com.baran.driver.Extras.Utils.showLocationSaveDialogue;

public class HomeFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private HomeViewModel homeViewModel;

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private boolean onRequestPermissionsResult;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    boolean isSourceSet = false, tripStarted = false, isPickupMode = true, isDropOffMode = false;
    private static final int DEFAULT_PICKUP_ZOOM = 17;
    private static final int DEFAULT_DROP_OFF_ZOOM = 12;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(30.2223, 71.4703);
    private LatLng pickUpLatLng, dropOffLatLng;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_LOCATION = "location";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private AutocompleteSupportFragment pickupAutoCompleteFragment, dropoffAutoCompleteFragment;
    private Marker pickUpMarker, dropOffMarker;
    private CameraPosition mCameraPosition;
    private Button btnConfirmPickup, btnConfirmDropOff, btnSkipDropOff, btnCallDriver,btnCancelRide;
    public static AppPreference appPreference;
    public Deque<String> stack;
    private int PICKUP_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int DROP_OFF_AUTOCOMPLETE_REQUEST_CODE = 2;
    private TextView pickupTextView, dropOffTextView;
    private ImageView pickUpSaveImage, dropOffSaveImage,dropOffIcon;
    private View separator;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private String dropOffText;
    String[] spinnerTitles;
    int[] spinnerImages;
    Spinner mSpinner;
    private boolean isUserInteracting;
    private User currentUser;
    private String vehicleType;
    CameraPosition cameraPosition ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        appPreference = MainActivity.appPreference;
        stack = new ArrayDeque<String>();

        currentUser = MainActivity.appPreference.getUserObject(getContext(),getActivity());

        View root = inflater.inflate(R.layout.passenger_fragment_home, container, false);

        separator = root.findViewById(R.id.tv_pickup_drop_off_separator);

        NavigationView mNavigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.nav_user_name);
        userName.setText(appPreference.getDisplayName());
        TextView userEmail = headerView.findViewById(R.id.nav_user_email);
        userEmail.setText(appPreference.getDisplayEmail());
        pickupTextView = root.findViewById(R.id.tv_pickup_location);
        dropOffTextView = root.findViewById(R.id.tv_drop_off_location);
        pickUpSaveImage = root.findViewById(R.id.img_pickup_location);
        dropOffSaveImage = root.findViewById(R.id.img_drop_off_location);
        dropOffIcon = root.findViewById(R.id.im_drop_off_icon);

        btnConfirmPickup = (Button) root.findViewById(R.id.btn_confirm_pickup);
        btnConfirmDropOff = (Button) root.findViewById(R.id.btn_confirm_drop_off);
        btnSkipDropOff = (Button) root.findViewById(R.id.btn_skip_drop_off);
        btnCallDriver = (Button) root.findViewById(R.id.btn_call_driver);
        btnCancelRide = root.findViewById(R.id.btn_cancel_ride);
//        TextView userEmail = headerView.findViewById(R.id.text_email_address);







        btnConfirmPickup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                pickupTextView.setClickable(false);
                dropOffTextView.setVisibility(View.VISIBLE);
                dropOffIcon.setVisibility(View.VISIBLE);
                separator.setVisibility(View.VISIBLE);
                pickUpMarker.setVisible(true);

                isPickupMode = false;
                isDropOffMode = true;

                v.setVisibility(View.GONE);
                btnConfirmDropOff.setVisibility(View.VISIBLE);
                btnSkipDropOff.setVisibility(View.VISIBLE);
                mSpinner.setVisibility(View.GONE);
                pickUpLatLng = pickUpMarker.getPosition();
                stack.push("CONFIRM_PICK_UP");

            }
        });


        btnConfirmDropOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dropOffTextView.setClickable(false);
                isDropOffMode = false;
                dropOffLatLng = dropOffMarker.getPosition();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(pickUpLatLng).include(dropOffLatLng);
                //Animate to the bounds
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 400, 400, 5);
                mMap.moveCamera(cameraUpdate);
                v.setVisibility(View.GONE);
                stack.push("CONFIRM_DROP_OFF");
                btnCallDriver.setVisibility(View.VISIBLE);
            }
        });


        btnSkipDropOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dropOffTextView.setVisibility(View.GONE);
                dropOffIcon.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
                isPickupMode = false;
                isDropOffMode = false;
                v.setVisibility(View.GONE);
                btnConfirmDropOff.setVisibility(View.GONE);
                btnCallDriver.setVisibility(View.VISIBLE);
                stack.push("SKIP_DROP_OFF");
                dropOffLatLng = null;
            }
        });


        btnCallDriver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                btnSkipDropOff.setVisibility(View.GONE);
                btnConfirmDropOff.setVisibility(View.GONE);
                btnConfirmPickup.setVisibility(View.GONE);
                mSpinner.setVisibility(View.GONE);
                stack.push("DRIVER_CALLED");
                String strPickupLatLng="";
                String strDropOffLatLng="";
                if(pickUpLatLng!=null){
                    strPickupLatLng =pickUpLatLng.latitude+","+pickUpLatLng.longitude;
                }
                if(dropOffLatLng!=null){
                    strDropOffLatLng =dropOffLatLng.latitude+","+dropOffLatLng.longitude;
                }

                Call<Ride> userCall = Passenger.ridesApi.findDriver(currentUser.getMobile(),vehicleType, strPickupLatLng,strDropOffLatLng);
                Utils.showProgressBarSpinner(getContext());
                userCall.enqueue(new Callback<Ride>() {
                    @Override
                    public void onResponse(Call<Ride> call, Response<Ride> response) {

                        if(response.isSuccessful()){
                                if(response.body().getResponse().equals("no_driver_found")){
                                    Utils.dismissProgressBarSpinner();
                                    showAlertBox(getActivity(),response.body().getMessage());
                                    stack.pop();
                                }else if(response.body().getResponse().equals("alert_sent_to_driver")){
                                    appPreference.setRideObject(response.body());
                                    showAlertBox(getActivity(),"Finding Driver for you!");
                                }
                        }else{
                            showAlertBox(getActivity(),"Unable to get the results from Server!");
                        }
                    }

                    @Override
                    public void onFailure(Call<Ride> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                        stack.pop();
                        showAlertBox(getActivity(),"Unable to connect to server");
                    }
                });



            }
        });






        pickUpSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) v.getTag(R.id.is_saved)) {
                    removeSavedLocationDialogue(getContext(), v,pickUpSaveImage);

                } else {
                    showLocationSaveDialogue(getContext(), v, pickUpLatLng, pickUpSaveImage, pickupTextView);
                }
            }
        });



        dropOffSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) v.getTag(R.id.is_saved)) {
                    removeSavedLocationDialogue(getContext(), v,dropOffSaveImage);

                } else {
                    showLocationSaveDialogue(getContext(), v, dropOffLatLng, dropOffSaveImage, dropOffTextView);
                }
            }
        });


        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        btnCancelRide.setOnClickListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyCMeNFGFvsheMOXo7gcJMMiLStrKyHGAFI");
        }


        pickupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivityForResult(intent, PICKUP_AUTOCOMPLETE_REQUEST_CODE);// Activity is started with requestCode 2
            }
        });


        dropOffTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivityForResult(intent, DROP_OFF_AUTOCOMPLETE_REQUEST_CODE);// Activity is started with requestCode 2
            }
        });

        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (!stack.isEmpty())
                        switch (stack.peek()) {
                            case "DRIVER_CALLED":
                                return true;
                            case "SKIP_DROP_OFF":
                            case "CONFIRM_DROP_OFF":

                                if (dropOffTextView.getVisibility() != View.VISIBLE) {
                                    dropOffTextView.setVisibility(View.VISIBLE);
                                    dropOffIcon.setVisibility(View.VISIBLE);
                                    separator.setVisibility(View.VISIBLE);
                                }

                                dropOffTextView.setClickable(true);
                                isPickupMode = false;

                                btnSkipDropOff.setVisibility(View.VISIBLE);
                                btnConfirmDropOff.setVisibility(View.VISIBLE);

                                btnConfirmPickup.setVisibility(View.GONE);
                                mSpinner.setVisibility(View.GONE);
                                btnCallDriver.setVisibility(View.GONE);

                                dropOffMarker.setVisible(false);
                                dropOffMarker.setPosition(pickUpLatLng);


                                cameraPosition = new CameraPosition.Builder().target(pickUpLatLng).zoom(DEFAULT_PICKUP_ZOOM).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                dropOffTextView.setText("Please Select Drop Off Location");
                                isDropOffMode = true;

                                stack.pop();
                                return true;
                            case "CONFIRM_PICK_UP":
                                dropOffTextView.setVisibility(View.GONE);
                                dropOffIcon.setVisibility(View.GONE);
                                separator.setVisibility(View.GONE);
                                dropOffSaveImage.setVisibility(View.GONE);
                                isDropOffMode = false;
                                isPickupMode = true;
                                btnConfirmPickup.setVisibility(View.VISIBLE);
                                mSpinner.setVisibility(View.VISIBLE);
                                btnSkipDropOff.setVisibility(View.GONE);
                                btnConfirmDropOff.setVisibility(View.GONE);
                                btnCallDriver.setVisibility(View.GONE);
                                dropOffMarker.setVisible(false);
                                cameraPosition = new CameraPosition.Builder().target(pickUpLatLng).zoom(DEFAULT_PICKUP_ZOOM).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                dropOffTextView.setText("Please Select Drop Off Location");
                                pickupTextView.setClickable(true);
                                stack.pop();
                                return true;
                            default:
                                return false;


                        }
                }
                return false;
            }
        });


        mSpinner = root.findViewById(R.id.spinner);
        spinnerTitles =  getResources().getStringArray(R.array.driver_types);

        spinnerImages = new int[]{
                R.drawable.ic_auto_512
                , R.drawable.ic_iconfinder_sedan_285810
                , R.drawable.ic_motor_bike_100
                };

        DriverTypeSpinnerAdapter mCustomAdapter = new DriverTypeSpinnerAdapter(getContext(),R.layout.driver_type_spiner_layout,spinnerTitles, spinnerImages);
        mSpinner.setAdapter(mCustomAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicleType=spinnerTitles[i];
                Toast.makeText(getContext(), spinnerTitles[i], Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    private BroadcastReceiver mRideAcceptedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Intent i = new Intent(getContext(), RideAlertActivity.class);
//            i.putExtras(intent.getExtras());
//            startActivityForResult(i, RIDE_ALERT_REQUEST_CODE);
            Utils.dismissProgressBarSpinner();
            Log.e("ride_accepted",intent.getStringExtra("driver_mobile"));
           showAlertBox(getActivity(),"Yo baby driver is coming to you.");
           btnCancelRide.setVisibility(View.VISIBLE);

        }
    };


    private BroadcastReceiver mDriverLocationReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Intent i = new Intent(getContext(), RideAlertActivity.class);
//            i.putExtras(intent.getExtras());
//            startActivityForResult(i, RIDE_ALERT_REQUEST_CODE);
            //Utils.dismissProgressBarSpinner();


            cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.valueOf(intent.getStringExtra("lat")),Double.valueOf(intent.getStringExtra("lng")))).zoom(DEFAULT_PICKUP_ZOOM).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            //showAlertBox(getActivity(),"Yo baby driver is coming to you.");


        }
    };


    @Override
    public void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((mRideAcceptedReceiver),
                new IntentFilter("RideAccepted")
        );

        LocalBroadcastManager.getInstance(getContext()).registerReceiver((mDriverLocationReceived),
                new IntentFilter("DriverLocationUpdate")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mRideAcceptedReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mDriverLocationReceived);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKUP_AUTOCOMPLETE_REQUEST_CODE) {
//            Log.e("res_code", "onActivityResult: requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);
//            Log.e("res_code", String.valueOf(resultCode));
            String placeName = "";
            if (resultCode == Activity.RESULT_OK) {
                boolean is_saved = data.getBooleanExtra("is_saved", false);
                if (!is_saved) {
                    pickUpSaveImage.setImageResource(R.drawable.ic_unsaved_icon);
                    Place place = (Place) data.getParcelableExtra("place");
                    placeName = place.getName();
                    pickUpLatLng = place.getLatLng();
                    pickUpSaveImage.setTag(R.id.is_saved, false);
                } else {
                    pickUpSaveImage.setImageResource(R.drawable.ic_saved_icon);
                    // TODO: Added logic to display saved content
                    SavedLocationData sl = (SavedLocationData) data.getParcelableExtra("location");
                    placeName = sl.getTitle();
                    pickUpLatLng = new LatLng(Double.parseDouble(sl.getLat()), Double.parseDouble(sl.getLng()));
                    pickUpSaveImage.setTag(R.id.is_saved, true);
                    pickUpSaveImage.setTag(R.id.location_db_id, sl.getId());
                    pickUpSaveImage.setTag(R.id.title, sl.getTitle());

                }
                pickUpSaveImage.setVisibility(View.VISIBLE);
                pickupTextView.setText(placeName);
                pickUpMarker.setPosition(pickUpLatLng);
                pickUpMarker.setVisible(true);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(pickUpLatLng).zoom(DEFAULT_PICKUP_ZOOM).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            } else {
                // The user canceled the operation.
                Log.e(TAG, "RESULT FUCKED UP");
            }

        } else if (requestCode == DROP_OFF_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean is_saved = data.getBooleanExtra("is_saved", false);
                String placeName = "";
                if (!is_saved) {
                    dropOffSaveImage.setImageResource(R.drawable.ic_unsaved_icon);
                    Place place = (Place) data.getParcelableExtra("place");
                    placeName = place.getName();
                    dropOffLatLng = place.getLatLng();
                    dropOffSaveImage.setTag(R.id.is_saved, false);

                } else {
                    dropOffSaveImage.setImageResource(R.drawable.ic_saved_icon);
                    SavedLocationData sl = (SavedLocationData) data.getParcelableExtra("location");
                    placeName = sl.getTitle();
                    dropOffLatLng = new LatLng(Double.parseDouble(sl.getLat()), Double.parseDouble(sl.getLng()));
                    dropOffSaveImage.setTag(R.id.is_saved, true);
                    dropOffSaveImage.setTag(R.id.location_db_id, sl.getId());
                    dropOffSaveImage.setTag(R.id.title, sl.getTitle());
                }
                dropOffSaveImage.setVisibility(View.VISIBLE);
                dropOffTextView.setText(placeName);
                dropOffMarker.setPosition(dropOffLatLng);
                dropOffMarker.setVisible(true);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(dropOffLatLng).zoom(DEFAULT_PICKUP_ZOOM).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                // The user canceled the operation.
                Log.e(TAG, "RESULT FUCKED UP");
            }
        }

        Log.e("I got it", "YOu babay bc");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
  getLocationPermission();



        updateLocationUI();



        getDeviceLocation();


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                LatLng latLng = mMap.getCameraPosition().target;


                if (isPickupMode) {
                    if (!pickUpMarker.isVisible()) {
                        pickUpMarker.setVisible(true);
                    }
                    pickUpMarker.setPosition(latLng);
                    pickUpLatLng = latLng;


                    PointF center = new PointF((float)pickUpLatLng.latitude, (float)pickUpLatLng.longitude);
                    final double mult = 1; // mult = 1.1; is more reliable
                    PointF p1 = calculateDerivedPosition(center, mult * 50, 0);
                    PointF p2 = calculateDerivedPosition(center, mult * 50, 90);
                    PointF p3 = calculateDerivedPosition(center, mult * 50, 180);
                    PointF p4 = calculateDerivedPosition(center, mult * 50, 270);




                    String strWhere =  " WHERE cast(lat as real) > " + String.valueOf(p3.x) + " AND cast(lat as real) < " + String.valueOf(p1.x) + " AND cast(lng as real)< " + String.valueOf(p2.y) + " AND cast(lng  as real) >" + String.valueOf(p4.y);

                    DBHelper d = new DBHelper(getContext());
                    SavedLocationData dox = d.getDataUsingLatLand(strWhere);
                    d.close();
                    if(dox!=null){
                        Log.e("got something",dox.getTitle());
                        Log.e("got something",String.valueOf(dox.getId()));
                    }


                }

                if (isDropOffMode) {
                    dropOffMarker.setPosition(latLng);
                    dropOffMarker.setVisible(true);
                    dropOffLatLng=latLng;
                }


            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
//                if(isPickupMode) {
////                    pickupAutoCompleteFragment.setText(Utils.getAddressUsingLatLong(getContext(), latLng.latitude, latLng.longitude));
//                }
//
//                if(isDropOffMode){
//                    dropoffAutoCompleteFragment.setText(Utils.getAddressUsingLatLong(getContext(), latLng.latitude, latLng.longitude));
//                }
            }
        });


        dropOffMarker = mMap.addMarker(new MarkerOptions()
                .position(mDefaultLocation)
                .draggable(true).icon(Utils.getBitmapFromVector(getContext(), R.drawable.ic_drop_off_locatin_marker)));
        dropOffMarker.setVisible(false);


        pickUpMarker = mMap.addMarker(new MarkerOptions()
                .position(mDefaultLocation)
                .draggable(true).icon(Utils.getBitmapFromVector(getContext(), R.drawable.ic_locatin_marker)));
        pickUpMarker.setVisible(false);


    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Exception: %s", e.getMessage());
                    }
                });
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_PICKUP_ZOOM));


//                                pickupAutoCompleteFragment.setText(Utils.getAddressUsingLatLong(getContext(), mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
                            }

                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_PICKUP_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
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
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_cancel_ride:
                    Ride r = appPreference.getRideObject();
                    Call<Ride> rideCall = Passenger.ridesApi.cancelRide(currentUser.getMobile(),String.valueOf(r.getId()));
                    Utils.showProgressBarSpinner(getContext());
                    rideCall.enqueue(new Callback<Ride>() {
                        @Override
                        public void onResponse(Call<Ride> call, Response<Ride> response) {
                            Utils.dismissProgressBarSpinner();
                            if(response.isSuccessful()){
                                    if(response.body().getResponse().equals("")){

                                    }
                            }else{

                            }
                        }

                        @Override
                        public void onFailure(Call<Ride> call, Throwable t) {

                        }
                    });
                    break;
            }
    }




//    @Override
//    public void onUserInteraction() {
//        super.onUserInteraction();
//        isUserInteracting = true;
//    }


}
