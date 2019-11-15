package com.baran.driver.ui.send;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Extras.AppPreference;
import com.baran.driver.R;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;
    public static AppPreference appPreference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appPreference = new AppPreference(getContext());

        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        final TextView textView = root.findViewById(R.id.text_send);
        sendViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


                        appPreference.setLoginStatus(false);
                        appPreference.setDisplayName("Name");
                        appPreference.setDisplayEmail("Email");
                        appPreference.setCreDate("DATE");

                        Log.e("fix", String.valueOf(appPreference.getLoginStatus()));


                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

        return root;
    }
}