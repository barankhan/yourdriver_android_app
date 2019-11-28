package com.baran.driver.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.DBHelper;
import com.baran.driver.Extras.AppPreference;
import com.baran.driver.Extras.SavedLocationData;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Passenger;
import com.baran.driver.R;

import com.baran.driver.SearchActivity;
import com.baran.driver.Services.MyInterface;
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


import static androidx.core.app.ActivityCompat.finishAffinity;
import static com.baran.driver.Extras.Utils.calculateDerivedPosition;
import static com.baran.driver.Extras.Utils.removeSavedLocationDialogue;
import static com.baran.driver.Extras.Utils.showLocationSaveDialogue;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private boolean onRequestPermissionsResult;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    boolean isSourceSet = false, tripStarted = false, isPickupMode = true, isDropOffMode = false;
    private static final int DEFAULT_PICKUP_ZOOM = 18;
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
    private Button btnConfirmPickup, btnConfirmDropOff, btnSkipDropOff, btnCallDriver;
    public static AppPreference appPreference;
    public Deque<String> stack;
    private int PICKUP_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int DROP_OFF_AUTOCOMPLETE_REQUEST_CODE = 2;
    private TextView pickupTextView, dropOffTextView;
    private ImageView pickUpSaveImage, dropOffSaveImage,dropOffIcon;
    private View separator;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private String dropOffText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        appPreference = new AppPreference(getContext());


        stack = new ArrayDeque<String>();

        View root = inflater.inflate(R.layout.fragment_home, container, false);

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

                stack.push("DRIVER_CALLED");
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
                    CameraPosition cameraPosition ;
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


        return root;
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
        // Add a pickUpMarker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(10).build();
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        // Prompt the user for permission.
        getLocationPermission();


        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.

        getDeviceLocation();

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                pickUpMarker.setPosition(latLng);
//                Log.e("fix","moved");
//                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//            }
//        });


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



}
