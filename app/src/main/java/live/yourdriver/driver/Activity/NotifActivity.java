package live.yourdriver.driver.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.DriverServerResponse;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.RidesApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;;


public class NotifActivity extends AppCompatActivity {
    private Button btnOkay;
    private TextView tvAlertMessage;
    private static Ringtone r=null;
    private ImageView imCallStart,imCallEnd;
    private String agoraChannel;
    private int rideId;
    private static boolean goToLogin=false;
    public static RidesApi ridesApi;
    public static AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        btnOkay = findViewById(R.id.btn_pop_up_ok);
        tvAlertMessage = findViewById(R.id.tv_pop_notification_msg);
        imCallStart = findViewById(R.id.im_attend_call);
        imCallEnd = findViewById(R.id.im_end_call);
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
        appPreference = new AppPreference(this);



        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().containsKey("message")) {
                tvAlertMessage.setText(getIntent().getExtras().getString("message"));
            }
            if(getIntent().getExtras().containsKey("goToLogin")) {
               if(getIntent().getExtras().getString("goToLogin").equals("yes")){
                   goToLogin=true;
               }
            }
            if(getIntent().getExtras().containsKey("setPickUpMode")) {
                if(getIntent().getExtras().getString("setPickUpMode").equals("true")){
                    appPreference.setIsPickupMode(true);
                }else if(getIntent().getExtras().getString("setPickUpMode").equals("false")){
                    appPreference.setIsPickupMode(false);
                }
            }
            if(getIntent().getExtras().containsKey("setDropoffMode")) {
                if(getIntent().getExtras().getString("setDropoffMode").equals("true")){
                    appPreference.setIsDropoffMode(true);
                }else if(getIntent().getExtras().getString("setDropoffMode").equals("false")){
                    appPreference.setIsDropoffMode(false);
                }
            }


            if(getIntent().getExtras().containsKey("agora_channel")) {
                agoraChannel = getIntent().getExtras().getString("agora_channel");
                rideId = Integer.valueOf(getIntent().getExtras().getString("ride_id"));
                btnOkay.setVisibility(View.INVISIBLE);
                imCallStart.setVisibility(View.VISIBLE);
                imCallEnd.setVisibility(View.VISIBLE);
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    r = RingtoneManager.getRingtone(getApplicationContext(), notification);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        getWindow().setLayout((int)(width*.9),(int)(height*.25));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-40;
        getWindow().setAttributes(params);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON

        );




        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r.stop();
                finish();
                if(goToLogin){
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });


        imCallStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotifActivity.this, VoiceChatViewActivity.class);
                intent.putExtra("agora_channel", agoraChannel);
                intent.putExtra("ride_id", rideId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        imCallEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = appPreference.getUserObjectWithoutUserValidation();
                if(u!=null){
                    Call<DriverServerResponse> rejectCall = ridesApi.rejectAgoraCall(u.getMobile(),agoraChannel,rideId);
                    Utils.showProgressBarSpinner(NotifActivity.this);
                    rejectCall.enqueue(new Callback<DriverServerResponse>() {
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
                }else{
                    finish();
                }

            }
        });
    }


    @Override
    public void onStop () {
//do your stuff here
        super.onStop();
        r.stop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(r!=null){
            r.play();
        }
    }
}
