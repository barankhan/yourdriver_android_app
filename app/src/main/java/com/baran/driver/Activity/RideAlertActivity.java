package com.baran.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baran.driver.Constants.Constant;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.RetrofitClient;
import com.baran.driver.Services.RidesApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.baran.driver.Extras.Utils.showAlertBox;

public class RideAlertActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    public static RidesApi ridesApi;
    private GoogleMap mMap;
    private MapView mv;
    double lat=0;
    double lng=0;
    String ride_id;
    TextView countDown;
    ProgressBar progressBar;
    Button btnAcceptRide,btnRejectRide;
    User currentUser;
    CountDownTimer timer;
    private static MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_alert);
        mediaPlayer = MediaPlayer.create(this, R.raw.ride_alert);
        mediaPlayer.start();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON

        );

        countDown = findViewById(R.id.tv_count_down);
        progressBar = findViewById(R.id.progressBar3);
        progressBar.setProgress(15);
        progressBar.setMax(15);
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);

        currentUser  = MainActivity.appPreference.getUserObject(this,this);

        btnAcceptRide = findViewById(R.id.btn_accept_ride);
        btnRejectRide = findViewById(R.id.btn_reject_ride);

        btnAcceptRide.setOnClickListener(this);


        timer =  new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //this will be done every 1000 milliseconds ( 1 seconds )
                int progress = (int) ((15000 - millisUntilFinished) / 1000);
                countDown.setText(String.valueOf(15-progress));
                progressBar.setProgress(14-progress);
            }

            @Override
            public void onFinish() {
                //the progressBar will be invisible after 60 000 miliseconds ( 1 minute)
                progressBar.setProgress(0);
            }

        }.start();


        if(getIntent().getExtras().containsKey("lat") && getIntent().getExtras().containsKey("lng") && getIntent().getExtras().containsKey("ride_id")){
            lat = Double.valueOf(getIntent().getExtras().getString("lat"));
            lng = Double.valueOf(getIntent().getExtras().getString("lng"));
            ride_id = getIntent().getExtras().getString("ride_id");
        }



        mv = findViewById(R.id.rideAlertMapView);
        mv.onCreate(savedInstanceState);
        mv.onResume();
        mv.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat,lng)).zoom(16).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title("Passenger Pickup Point"));

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btn_accept_ride) {
            timer.cancel();
            Utils.showProgressBarSpinner(this);
            Call<Ride> userCall = this.ridesApi.acceptRide(currentUser.getMobile(), getIntent().getExtras().getString("ride_id"),MainActivity.appPreference.getLat(),MainActivity.appPreference.getLng());
            userCall.enqueue(new Callback<Ride>() {
                @Override
                public void onResponse(Call<Ride> call, Response<Ride> response) {
                    Utils.dismissProgressBarSpinner();
                    if (response.isSuccessful()) {
                        if (response.body().getResponse().equals("you_got_it")) {
                            MainActivity.appPreference.setRideObject(response.body());
                            DriverActivity.currentRide = response.body();
                            showAlertBox(RideAlertActivity.this, "Congratulation! You got the Ride!");
                            finish();
                        } else {
                            showAlertBox(RideAlertActivity.this, "Sorry! Someone else got it.");
                        }

                    } else {
                        showAlertBox(RideAlertActivity.this, "something went wrong!");
                    }
                }

                @Override
                public void onFailure(Call<Ride> call, Throwable t) {
                    Utils.dismissProgressBarSpinner();
                    showAlertBox(RideAlertActivity.this, "Unable to connect to server");
                }
            });
        }
    }
}
