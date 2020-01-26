package live.yourdriver.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Model.User;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.FrameLayout;

import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Fragments.DriverDataUpdateFragmentStep1;
import live.yourdriver.driver.Fragments.DriverDataUpdateFragmentStep2;
import live.yourdriver.driver.Fragments.ForgetPasswordFragment;
import live.yourdriver.driver.Fragments.LoginFragment;
import live.yourdriver.driver.Fragments.RegistrationVerificationFragment;
import live.yourdriver.driver.Fragments.RegistrationFragment;

import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.MyInterface;
import live.yourdriver.driver.Services.NotificationAlertReceiver;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.ServiceApi;


public class MainActivity extends AppCompatActivity implements MyInterface {

    public static AppPreference appPreference;
    public static String c_date;

    FrameLayout container_layout;
    NotificationAlertReceiver notifReceiver;
    IntentFilter notifIntentFilter;
    public static ServiceApi serviceApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container_layout = findViewById(R.id.fragment_container);
        
        appPreference = new AppPreference(this);

//        notifReceiver = new NotificationAlertReceiver();
//        notifIntentFilter = new IntentFilter("com.barankhan.driver.notif_alerts");
//        registerReceiver(notifReceiver, notifIntentFilter);

        //Log.e("created_at: ", c_date);

        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_USERS_API).create(ServiceApi.class);
//        this.driverDataUpdateStep2();
        if (container_layout != null){
            if (savedInstanceState != null){
                return;
            }
            this.loginFragment();

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
    public void login(User u) {
        if(u==null){
            MainActivity.appPreference.setLoginStatus(false);
            loginFragment();
        }
        appPreference.setDisplayName(u.getName());
        appPreference.setDisplayEmail(u.getEmail());
        appPreference.setCreDate(u.getCreatedAt());
        if(u.getIsVerified()==1 && u.getId()>0 && u.getIsDriver()==0){
            Intent intent = new Intent(this, Passenger.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(u.getIsVerified()==1 && u.getId()>0 && u.getIsDriver()==1){
            Intent intent = new Intent(this, DriverActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if(u.getId()>0 && u.getIsVerified()==0){
            registrationVerificationFragment();
        }
    }
    @Override
    public void logout() {
        appPreference.setLoginStatus(false);
        appPreference.setDisplayName("Name");
        appPreference.setDisplayEmail("Email");
        appPreference.setCreDate("DATE");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void registrationVerificationFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegistrationVerificationFragment())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void loginFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void forgetPassword() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ForgetPasswordFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void driverDataUpdateStep1() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DriverDataUpdateFragmentStep1())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void driverDataUpdateStep2() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new DriverDataUpdateFragmentStep2())
                .addToBackStack(null)
                .commit();
    }


}
