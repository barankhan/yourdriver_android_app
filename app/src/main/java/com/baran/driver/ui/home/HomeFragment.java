package com.baran.driver.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.baran.driver.Extras.AppPreference;
import com.baran.driver.Extras.Utils;
import com.baran.driver.R;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private boolean onRequestPermissionsResult;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    boolean isSourceSet = false, tripStarted = false,isPickupMode=true,isDropOffMode=false;
    private static final int DEFAULT_PICKUP_ZOOM = 18;
    private static final int DEFAULT_DROP_OFF_ZOOM = 12;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(30.2223, 71.4703);
    private LatLng pickUpLatLng,dropOffLatLng ;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_LOCATION = "location";
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private AutocompleteSupportFragment pickupAutoCompleteFragment, dropoffAutoCompleteFragment;
    private Marker pickUpMarker,dropOffMarker;
    private CameraPosition mCameraPosition;
    private Button btnConfirmPickup, btnConfirmDropOff,btnSkipDropOff,btnCallDriver;
    public static AppPreference appPreference;
    public Deque<String> stack;
    private int AUTOCOMPLETE_REQUEST_CODE = 1;

    private static final String TAG = HomeFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        appPreference = new AppPreference(getContext());

        stack = new ArrayDeque<String>();

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        NavigationView mNavigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.nav_user_name);
        userName.setText(appPreference.getDisplayName());
        TextView userEmail = headerView.findViewById(R.id.nav_user_email);
        userEmail.setText(appPreference.getDisplayEmail());

        btnConfirmPickup = (Button) root.findViewById(R.id.btn_confirm_pickup);
        btnConfirmDropOff = (Button) root.findViewById(R.id.btn_confirm_drop_off);
        btnSkipDropOff = (Button) root.findViewById(R.id.btn_skip_drop_off);
        btnCallDriver = (Button) root.findViewById(R.id.btn_call_driver);
//        TextView userEmail = headerView.findViewById(R.id.text_email_address);


        btnConfirmPickup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dropoffAutoCompleteFragment.getView().setVisibility(View.VISIBLE);
                pickUpMarker.setVisible(true);

                isPickupMode=false;
                isDropOffMode=true;

                v.setVisibility(View.GONE);
                btnConfirmDropOff.setVisibility(View.VISIBLE);
                btnSkipDropOff.setVisibility(View.VISIBLE);
                pickUpLatLng = pickUpMarker.getPosition();
                stack.push("CONFIRM_PICK_UP");

            }
        });



        btnConfirmDropOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                isDropOffMode=false;
                dropOffLatLng = dropOffMarker.getPosition();
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(pickUpLatLng).include(dropOffLatLng);
                //Animate to the bounds
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 100);
                mMap.moveCamera(cameraUpdate);
                v.setVisibility(View.GONE);
                stack.push("CONFIRM_DROP_OFF");
                btnCallDriver.setVisibility(View.VISIBLE);
            }
        });





        btnSkipDropOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dropoffAutoCompleteFragment.getView().setVisibility(View.GONE);
                isPickupMode=false;
                isDropOffMode=false;
                v.setVisibility(View.GONE);
                btnConfirmDropOff.setVisibility(View.GONE);
                btnCallDriver.setVisibility(View.VISIBLE);
                stack.push("SKIP_DROP_OFF");
                dropOffLatLng=null;
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


        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }


        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
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

        // Pickup AutoComplete Fragment
         pickupAutoCompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.pickup_autocomplete_fragment);
//        pickupAutoCompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getContext());


        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);


        pickupAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(place.getLatLng().latitude,
                                place.getLatLng().longitude), DEFAULT_PICKUP_ZOOM));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });

//        pickupAutoCompleteFragment.setLocationRestriction();



        dropoffAutoCompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.drop_off_autocomplete_fragment);
        dropoffAutoCompleteFragment.getView().setVisibility(View.INVISIBLE);
        dropoffAutoCompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

        dropoffAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(place.getLatLng().latitude,
                                place.getLatLng().longitude), DEFAULT_PICKUP_ZOOM));
                dropOffMarker.setPosition(place.getLatLng());
                dropOffMarker.setVisible(true);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });


        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK  && event.getAction()== KeyEvent.ACTION_DOWN )
                {
                    switch (stack.peek()){
                        case "DRIVER_CALLED":
                            return true;
                        case "SKIP_DROP_OFF":
                        case "CONFIRM_DROP_OFF":

                            if(dropoffAutoCompleteFragment.getView().getVisibility()!=View.VISIBLE)
                                dropoffAutoCompleteFragment.getView().setVisibility(View.VISIBLE);


                            isPickupMode=false;

                            btnSkipDropOff.setVisibility(View.VISIBLE);
                            btnConfirmDropOff.setVisibility(View.VISIBLE);

                            btnConfirmPickup.setVisibility(View.GONE);
                            btnCallDriver.setVisibility(View.GONE);

                            dropOffMarker.setVisible(false);
                            dropOffMarker.setPosition(pickUpLatLng);


                            CameraPosition cameraPosition = new CameraPosition.Builder().target(pickUpLatLng).zoom(DEFAULT_PICKUP_ZOOM).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            dropoffAutoCompleteFragment.setText(" ");
                            isDropOffMode=true;

                            stack.pop();
                            return true;
                        case "CONFIRM_PICK_UP":
                            dropoffAutoCompleteFragment.getView().setVisibility(View.GONE);
                            isDropOffMode=false;
                            isPickupMode=true;
                            btnConfirmPickup.setVisibility(View.VISIBLE);
                            btnSkipDropOff.setVisibility(View.GONE);
                            btnConfirmDropOff.setVisibility(View.GONE);
                            btnCallDriver.setVisibility(View.GONE);
                            dropOffMarker.setVisible(false);
                            stack.pop();
                            return true;
                        default:
                            return false;


                    }
                }
                return false;
            }
        } );


        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            Log.e("res_code", "onActivityResult: requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);
            Log.e("res_code", String.valueOf(resultCode));

            Place place = Autocomplete.getPlaceFromIntent(data);
            Log.e(TAG, "Place: " + place.getName() + ", " + place.getId());
            if (resultCode == Activity.RESULT_OK) {
                Place place1 = Autocomplete.getPlaceFromIntent(data);
                Log.e(TAG, "Place: " + place1.getName() + ", " + place1.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.e(TAG, "RESULT FUCKED UP");
            }
        Log.e("I got it","YOu babay");
        }
        Log.e("I got it","YOu babay bc");
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


                if(isPickupMode){
                    if(!pickUpMarker.isVisible()) {
                        pickUpMarker.setVisible(true);
                    }
                    pickUpMarker.setPosition(latLng);
                }

                if(isDropOffMode){
                    dropOffMarker.setPosition(latLng);
                    dropOffMarker.setVisible(true);
                }


            }
        });


        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                if(isPickupMode) {
                    pickupAutoCompleteFragment.setText(Utils.getAddressUsingLatLong(getContext(), latLng.latitude, latLng.longitude));
                }

                if(isDropOffMode){
                    dropoffAutoCompleteFragment.setText(Utils.getAddressUsingLatLong(getContext(), latLng.latitude, latLng.longitude));
                }
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

                            if(mLastKnownLocation !=null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_PICKUP_ZOOM));


                                pickupAutoCompleteFragment.setText(Utils.getAddressUsingLatLong(getContext(), mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()));
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
        } catch (SecurityException e)  {
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
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

}
