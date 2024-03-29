package live.yourdriver.driver.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Fragments.ChangePasswordFragment;
import live.yourdriver.driver.Fragments.driver.alerts.RideAlertsFragment;
import live.yourdriver.driver.Fragments.driver.dashboard.DashboardFragment;
import live.yourdriver.driver.Fragments.driver.recharge.RechargeFragment;
import live.yourdriver.driver.Fragments.driver.tickets.TicketsFragment;
import live.yourdriver.driver.Fragments.driver.transactions.TransactionsFragment;
import live.yourdriver.driver.Fragments.driver.home.HomeFragment;
import live.yourdriver.driver.Fragments.passenger.logout.LogoutFragment;
import live.yourdriver.driver.Model.Ride;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.ChatHeadService;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.RidesApi;

import android.os.PowerManager;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class DriverActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static Ride currentRide = null;
    public static RidesApi ridesApi;

    private View headerView;
    public static AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appPreference = new AppPreference(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }


        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
        final DrawerLayout drawer = findViewById(R.id.d_drawer_layout);
        NavigationView navigationView = findViewById(R.id.d_nav_view);

        headerView = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.d_nav_home, R.id.d_nav_transactions
//                , R.id.d_nav_slideshow
//                ,R.id.d_nav_tools, R.id.d_nav_share
        )
                .setDrawerLayout(drawer)
                .build();


        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING && !drawer.isDrawerOpen(GravityCompat.START)) {
                    User currentUser  = appPreference.getUserObjectWithoutUserValidation();
                    TextView driverBalance = headerView.findViewById(R.id.tv_driver_balance);
                    driverBalance.setText("Balance: "+String.valueOf(currentUser.getBalance()));
                    TextView rating = headerView.findViewById(R.id.tv_acceptance_rating);
                    rating.setText("Rating: "+String.valueOf(currentUser.getRating())+", "+String.valueOf(currentUser.getAcceptancePoints()));
                    TextView tvRides = headerView.findViewById(R.id.tv_total_rides);
                    tvRides.setText("Total Rides: "+String.valueOf(currentUser.getTotalRides()));

                }
            }
        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem,drawer);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem,DrawerLayout drawer) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.d_nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.d_nav_transactions:
                fragmentClass = TransactionsFragment.class;
                break;
            case R.id.d_nav_logout:
                fragmentClass = LogoutFragment.class;
                break;
            case R.id.d_nav_support:
                fragmentClass = TicketsFragment.class;
                break;
            case R.id.d_nav_recharge:
                fragmentClass = RechargeFragment.class;
                break;
            case R.id.d_nav_password:
                fragmentClass = ChangePasswordFragment.class;
                break;
            case R.id.d_nav_ride_alert:
                fragmentClass = RideAlertsFragment.class;
                break;
            case R.id.d_nav_dash:
                fragmentClass = DashboardFragment.class;
                break;
//            case R.id.d_switch_to_passenger:
//                fragmentClass = null;
//                break;
        }


        if(fragmentClass!=null) {

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("@").commit();

        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.driver, menu);
//        return true;
//    }


    @Override
    protected void onStop() {
        super.onStop();
        User u = appPreference.getUserObject(this,this);
        if(u!=null) {
            if (appPreference.getUserObject(this, this).getIsDriverOnline() == 1)
                startService(new Intent(this, ChatHeadService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ChatHeadService.running)
            stopService(new Intent(this, ChatHeadService.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
