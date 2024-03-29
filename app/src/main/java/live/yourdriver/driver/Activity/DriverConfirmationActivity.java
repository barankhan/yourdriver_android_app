package live.yourdriver.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.R;;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DriverConfirmationActivity extends AppCompatActivity {

    TextView congratsLabel;
    Button btnLogin;
    public static AppPreference appPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_confirmation);
        congratsLabel = findViewById(R.id.txt_driver_confirmation_message);
        btnLogin = findViewById(R.id.btnLogin);
        appPreference = new AppPreference(this);
        if (getIntent().getExtras() != null) {
            if(getIntent().getExtras().containsKey("do")){
                String value = getIntent().getExtras().getString("do");
//                Log.e("value",value);
                if(value.equals("logout")){
                    appPreference.setLoginStatus(false);
                    btnLogin.setVisibility(View.VISIBLE);
                }
            }
            if(getIntent().getExtras().containsKey("msg")){
                String value = getIntent().getExtras().getString("msg");
                congratsLabel.setText(value);
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(getBaseContext(), MainActivity.class);
                startMain.addFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });
    }
}
