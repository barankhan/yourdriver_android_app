package com.baran.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.baran.driver.R;


public class NotifActivity extends Activity {
    private Button btnOkay;
    private TextView tvAlertMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        btnOkay = findViewById(R.id.btn_pop_up_ok);
        tvAlertMessage = findViewById(R.id.tv_pop_notification_msg);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().containsKey("message")) {
                tvAlertMessage.setText(getIntent().getExtras().getString("message"));
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


        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
