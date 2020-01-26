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

import live.yourdriver.driver.R;;


public class NotifActivity extends Activity {
    private Button btnOkay;
    private TextView tvAlertMessage;
    private static Ringtone r=null;
    private ImageView imCallStart,imCallEnd;
    private String agoraChannel;
    private int rideId;
    private static boolean goToLogin=false;
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
                    MainActivity.appPreference.setIsPickupMode(true);
                }else if(getIntent().getExtras().getString("setPickUpMode").equals("false")){
                    MainActivity.appPreference.setIsPickupMode(false);
                }
            }
            if(getIntent().getExtras().containsKey("setDropoffMode")) {
                if(getIntent().getExtras().getString("setDropoffMode").equals("true")){
                    MainActivity.appPreference.setIsDropoffMode(true);
                }else if(getIntent().getExtras().getString("setDropoffMode").equals("false")){
                    MainActivity.appPreference.setIsDropoffMode(false);
                }
            }


            if(getIntent().getExtras().containsKey("agora_channel")) {
                agoraChannel = getIntent().getExtras().getString("agora_channel");
                rideId = getIntent().getExtras().getInt("ride_id");
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