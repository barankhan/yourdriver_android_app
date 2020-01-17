package com.baran.driver.Fragments.passenger.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import okhttp3.internal.Util;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Extras.AppPreference;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Fragments.driver.home.HomeFragment;
import com.baran.driver.Model.User;
import com.baran.driver.R;

public class LogoutFragment extends Fragment {

    private LogoutViewModel sendViewModel;
    public static AppPreference appPreference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appPreference = new AppPreference(getContext());

        sendViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.passenger_fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        User u = appPreference.getUserObject(getContext(),getActivity());
        if(u.getIsDriverOnline()==1 && u.getIsDriver()==1){
            Utils.showAlertBox(getActivity(),"You Can't Logout, Please go offline first!");
            FragmentManager fragmentManager = getFragmentManager();
            Fragment fragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.d_nav_host_fragment, fragment).addToBackStack("@").commit();
        }else{
            appPreference.setLoginStatus(false);
            appPreference.setUserObject(null);
            appPreference.setDisplayName("Name");
            appPreference.setDisplayEmail("Email");
            appPreference.setCreDate("DATE");

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }


        return root;
    }
}