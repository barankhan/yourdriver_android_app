package com.baran.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.baran.driver.Constants.Constant;
import com.baran.driver.Extras.AppPreference;
import com.baran.driver.Fragments.LoginFragment;
import com.baran.driver.Fragments.ProfileFragment;
import com.baran.driver.Fragments.RegistrationFragment;
import com.baran.driver.R;
import com.baran.driver.Services.MyInterface;
import com.baran.driver.Services.RetrofitClient;
import com.baran.driver.Services.ServiceApi;
import com.baran.driver.Passenger;


public class MainActivity extends AppCompatActivity implements MyInterface {

    public static AppPreference appPreference;
    public static String c_date;

    FrameLayout container_layout;

    public static ServiceApi serviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container_layout = findViewById(R.id.fragment_container);
        appPreference = new AppPreference(this);

        //Log.e("created_at: ", c_date);

        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL).create(ServiceApi.class);

        if (container_layout != null){
            if (savedInstanceState != null){
                return;
            }

            //check login status from sharedPreference
            if (appPreference.getLoginStatus()){
                //when true
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .add(R.id.fragment_container, new ProfileFragment())
//                        .commit();
                Intent intent = new Intent(this, Passenger.class);
                startActivity(intent);
            } else {
                // when get false
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragment_container, new LoginFragment())
                        .commit();
            }
        }

    } // ending onCreate


    // overridden from MyInterface
    @Override
    public void register() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegistrationFragment())
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void login(String name, String email, String created_at) {
        appPreference.setDisplayName(name);
        appPreference.setDisplayEmail(email);
        appPreference.setCreDate(created_at);

//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, new ProfileFragment())
//                .commit();

        Intent intent = new Intent(this, Passenger.class);
        startActivity(intent);

    }
    @Override
    public void logout() {
        appPreference.setLoginStatus(false);
        appPreference.setDisplayName("Name");
        appPreference.setDisplayEmail("Email");
        appPreference.setCreDate("DATE");

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
