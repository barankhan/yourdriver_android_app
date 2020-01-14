package com.baran.driver.Activity;

import android.os.Bundle;

import com.baran.driver.Constants.Constant;
import com.baran.driver.Fragments.driver.recharge.RechargeFragment;
import com.baran.driver.Fragments.driver.transactions.TransactionsFragment;
import com.baran.driver.Fragments.driver.home.HomeFragment;
import com.baran.driver.Fragments.passenger.logout.LogoutFragment;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.RetrofitClient;
import com.baran.driver.Services.RidesApi;

import android.util.Log;
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

import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class DriverActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static Ride currentRide = null;
    public static RidesApi ridesApi;

    private View headerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
        final DrawerLayout drawer = findViewById(R.id.d_drawer_layout);
        NavigationView navigationView = findViewById(R.id.d_nav_view);

        headerView = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.d_nav_home, R.id.d_nav_transactions, R.id.d_nav_slideshow,
                R.id.d_nav_tools, R.id.d_nav_share)
                .setDrawerLayout(drawer)
                .build();


        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING && !drawer.isDrawerOpen(GravityCompat.START)) {
                    User currentUser  = MainActivity.appPreference.getUserObjectWithoutUserValidation();
                    Log.e("drawer opening","here we go");
                    TextView driverBalance = headerView.findViewById(R.id.tv_driver_balance);
                    driverBalance.setText(String.valueOf(currentUser.getBalance()));
                }
            }
        });
        NavController navController = Navigation.findNavController(this, R.id.d_nav_host_fragment);
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
            case R.id.d_nav_recharge:
                fragmentClass = RechargeFragment.class;
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
            fragmentManager.beginTransaction().replace(R.id.d_nav_host_fragment, fragment).addToBackStack("@").commit();

        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.driver, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.d_nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
