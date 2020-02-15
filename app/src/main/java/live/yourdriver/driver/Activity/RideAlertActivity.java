package live.yourdriver.driver.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import live.yourdriver.driver.Model.DriverServerResponse;
import live.yourdriver.driver.Model.Ride;
import live.yourdriver.driver.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.UserRide;
import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.RidesApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static live.yourdriver.driver.Extras.Utils.showAlertBox;
import static live.yourdriver.driver.Extras.Utils.showAlertBoxThenFinish;

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
    boolean rejectAlert = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_alert);


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
        btnRejectRide.setOnClickListener(this);

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
                if(!rejectAlert)
                    finish();
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
            mediaPlayer.stop();
            Utils.showProgressBarSpinner(this);
            Call<UserRide> userCall = this.ridesApi.acceptRide(currentUser.getMobile(), getIntent().getExtras().getString("ride_id"),MainActivity.appPreference.getLat(),MainActivity.appPreference.getLng());
            userCall.enqueue(new Callback<UserRide>() {
                @Override
                public void onResponse(Call<UserRide> call, Response<UserRide> response) {
                    Utils.dismissProgressBarSpinner();
                    if (response.isSuccessful()) {
                        if (response.body().getResponse().equals("you_got_it")) {
                            MainActivity.appPreference.setRideObject(response.body().getRide());
                            MainActivity.appPreference.setPassengerObject(response.body().getUser());
                            finish();
                        } else {
                            showAlertBoxThenFinish(RideAlertActivity.this, "Sorry! Someone else got it.");
                        }

                    } else {
                        showAlertBox(RideAlertActivity.this, "something went wrong!");
                    }
                }

                @Override
                public void onFailure(Call<UserRide> call, Throwable t) {
                    Utils.dismissProgressBarSpinner();
//                    Log.e("Error iN accept",t.toString());
                    showAlertBox(RideAlertActivity.this, "Unable to connect to server");
                }
            });
        }else if(v.getId()==R.id.btn_reject_ride){


            rejectAlert=true;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog d =builder.setTitle("Do you want to reject the ride?").setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timer.cancel();
                    mediaPlayer.stop();
                    Utils.showProgressBarSpinner(RideAlertActivity.this);
                    Call<DriverServerResponse> userCall = ridesApi.rejectRide(currentUser.getId(), getIntent().getExtras().getString("ride_id"));
                    userCall.enqueue(new Callback<DriverServerResponse>() {
                        @Override
                        public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                            Utils.dismissProgressBarSpinner();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                            Utils.dismissProgressBarSpinner();
                            finish();
                        }
                    });
                }
            }).setNegativeButton(R.string.no,null).create();

            d.show();

        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        mediaPlayer = MediaPlayer.create(this, R.raw.ride_alert);
        mediaPlayer.start();
    }

    @Override
    public void onStop () {
        super.onStop();
        mediaPlayer.stop();
    }


}
