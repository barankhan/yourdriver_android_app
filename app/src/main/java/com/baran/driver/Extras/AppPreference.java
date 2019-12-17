package com.baran.driver.Extras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Activity.Passenger;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.google.gson.Gson;

import androidx.core.content.ContextCompat;

// Shared Preference METHODS

public class AppPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public AppPreference(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(String.valueOf(R.string.s_pref_file), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //Setting login status
    public void setLoginStatus(boolean status){
        editor.putBoolean(String.valueOf(R.string.s_pref_login_status), status);
        editor.commit();
    }
    public boolean getLoginStatus(){
        return sharedPreferences.getBoolean(String.valueOf(R.string.s_pref_login_status), false);
    }

    // For Name
    public void setDisplayName(String name){
        editor.putString(String.valueOf(R.string.s_pref_name), name);
        editor.commit();
    }
    public String getDisplayName(){
        return sharedPreferences.getString(String.valueOf(R.string.s_pref_name), "Name");
    }

    //For email
    public void setDisplayEmail(String email){
        editor.putString(String.valueOf(R.string.s_pref_email), email);
        editor.commit();
    }
    public String getDisplayEmail(){
        return sharedPreferences.getString(String.valueOf(R.string.s_pref_email), "email");
    }

    //For email
    public void setCreDate(String date){
        editor.putString(String.valueOf(R.string.s_pref_date), date);
        editor.commit();
    }
    public String getCreDate(){
        return sharedPreferences.getString(String.valueOf(R.string.s_pref_date), "date");
    }

    // For TOAST Message for response
    public void showToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public void setUserObject(User u){
        Gson gson = new Gson();
        String json = gson.toJson(u); // myObject - instance of MyObject
        editor.putString("UserObj", json);
        editor.commit();
    }

    public void setRideObject(Ride r){
        Gson gson = new Gson();
        String json = gson.toJson(r); // myObject - instance of MyObject
        editor.putString("RideObj", json);
        editor.commit();
    }

    public Ride getRideObject(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("RideObj", null);
        return  gson.fromJson(json, Ride.class);
    }


    public User getUserObject(Context c,Activity a){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserObj", null);
        User user = gson.fromJson(json, User.class);

        if(user.getId()==0){
            Intent intent = new Intent(a, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(intent);
        }

        return  user;
    }

    public User getUserObjectWithoutUserValidation(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("UserObj", null);
        User user = gson.fromJson(json, User.class);
        return  user;
    }


    // For Lat
    public void setLat(String lat){
        editor.putString("lat", lat);
        editor.commit();
    }
    public String getLat(){
        return sharedPreferences.getString("lat", "lat");
    }


    // For Lat
    public void setLng(String lng){
        editor.putString("lng", lng);
        editor.commit();
    }
    public String getLng(){
        return sharedPreferences.getString("lng", "lng");
    }

}