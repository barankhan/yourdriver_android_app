package live.yourdriver.driver.Activity;

import android.content.Intent;
import android.os.Bundle;

import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Fragments.ChangePasswordFragment;
import live.yourdriver.driver.Fragments.DriverDataUpdateFragmentStep1;
import live.yourdriver.driver.Fragments.DriverDataUpdateFragmentStep2;
import live.yourdriver.driver.Fragments.ProfileFragment;
import live.yourdriver.driver.Fragments.ProfilePictureFragment;
import live.yourdriver.driver.Fragments.driver.tickets.TicketsFragment;
import live.yourdriver.driver.Fragments.passenger.dashboard.DashboardFragment;
import live.yourdriver.driver.Fragments.passenger.transactions.TransactionsFragment;
import live.yourdriver.driver.Fragments.passenger.home.HomeFragment;
import live.yourdriver.driver.Fragments.passenger.logout.LogoutFragment;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.RidesApi;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Passenger extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    NavigationView navigation;
    ActionBarDrawerToggle action;
    Button btnDrawerToggle;
    public static RidesApi ridesApi;
    public static View headerView;
    private ImageView imPassengerImage,imPassengerIcon;
    private User currentUser;
    public static AppPreference appPreference;


    private FragmentManager fragmentManager;
    public static  Picasso picasso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appPreference = new AppPreference(this);

        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
        currentUser = appPreference.getUserObjectWithoutUserValidation();
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);

        try
        {
            Picasso.Builder picassoBuilder = new Picasso.Builder(getApplicationContext());
            picassoBuilder.downloader(new OkHttp3Downloader(RetrofitClient.okClient()));
            picasso = picassoBuilder.build();
            Picasso.setSingletonInstance(picasso); //apply to default singleton instance
        }

        catch ( IllegalStateException e )
        {
            //TODO
        }




        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING && !drawer.isDrawerOpen(GravityCompat.START)) {
                    User currentUser  = appPreference.getUserObjectWithoutUserValidation();
                    TextView tvUserName = headerView.findViewById(R.id.nav_user_name);
                    TextView tvUserEmail = headerView.findViewById(R.id.nav_user_email);
                    TextView tvBalance = headerView.findViewById(R.id.nav_user_balance);
                    TextView tvMobile = headerView.findViewById(R.id.nav_user_mobile);

                    ImageView imPassengerIcon =  headerView.findViewById(R.id.im_passenger_image);
                    tvUserName.setText(currentUser.getName());
                    tvUserEmail.setText(currentUser.getEmail());
                    tvBalance.setText("Balance: "+currentUser.getBalance());
                    tvMobile.setText(currentUser.getMobile());







                }
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        imPassengerIcon =  headerView.findViewById(R.id.im_passenger_image);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_passenger_transactions, R.id.nav_profile,
//                R.id.nav_tools, R.id.nav_share,
                R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
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

        fragmentManager = getSupportFragmentManager();
        imPassengerImage = headerView.findViewById(R.id.im_passenger_image);
        imPassengerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, new ProfilePictureFragment()).addToBackStack("@").commit();
                drawer.closeDrawers();
            }
        });

        if(currentUser!=null){
            if(currentUser.getPicture()!=""){
                picasso.get().load(Constant.baseUrl.UPLOADS_URL+currentUser.getPicture()).noPlaceholder().fit().centerCrop()
                        .into(imPassengerIcon);

            }
        }



    }


    public void selectDrawerItem(MenuItem menuItem,DrawerLayout drawer) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_passenger_transactions:
                fragmentClass = TransactionsFragment.class;
                break;
            case R.id.nav_logout:
                fragmentClass = LogoutFragment.class;
                break;
            case R.id.nav_partner:
                fragmentClass = getBecomePartnerFragment();
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;

            case R.id.nav_support:
                fragmentClass = TicketsFragment.class;
                break;
            case R.id.nav_password:
                fragmentClass = ChangePasswordFragment.class;
                break;
            case R.id.nav_dash:
                fragmentClass = DashboardFragment.class;
                break;
        }


        if(fragmentClass!=null) {

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment

            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack("@").commit();

        }
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }

    private Class getBecomePartnerFragment(){
        User currentUser = appPreference.getUserObject(this,this);
        if(currentUser.getDriverSteps()==0){
            return DriverDataUpdateFragmentStep1.class;
        }else if(currentUser.getDriverSteps()==1){
            return DriverDataUpdateFragmentStep2.class;
        }else if(currentUser.getDriverSteps()==2 && currentUser.getIsDriver()==0){
            Utils.showAlertBox(this,"Your Request is under review. Please wait.");
        }else if(currentUser.getDriverSteps()==2 && currentUser.getIsDriver()==1){
            Utils.showAlertBox(this,"You are a partner Boss!");
        }
        return null;
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.passenger, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
