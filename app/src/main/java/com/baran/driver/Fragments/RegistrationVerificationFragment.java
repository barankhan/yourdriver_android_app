package com.baran.driver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.MyInterface;

import static com.baran.driver.Extras.Utils.showAlertBox;

public class RegistrationVerificationFragment extends Fragment {

    Button verificationButton;
    EditText token;
    User u;
    private MyInterface registrationFromActivityListener;

    public RegistrationVerificationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_verification, container, false);
        u = MainActivity.appPreference.getUserObject();
        verificationButton = view.findViewById(R.id.verificationButton);
        token = view.findViewById(R.id.et_verification_Token);

        if(u.getId()==0){
            registrationFromActivityListener.loginFragment();
        }


        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(token.getText().length()!=5)
                        MainActivity.appPreference.showToast("Please Enter 5 digit Verification Code");
                    else
                        verifyToken();
            }
        });



        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        registrationFromActivityListener = (MyInterface) activity;

    }


    private boolean verifyToken(){

        int tokenValue = Integer.valueOf(token.getText().toString());


        if(u.getVerificationToken()==tokenValue){
            Call<User> userCall = MainActivity.serviceApi.doTokenVerification(u.getMobile(),tokenValue);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                        if(response.body().getResponse().equals("verified")){
                            MainActivity.appPreference.setUserObject(response.body());
                            MainActivity.appPreference.setLoginStatus(true);
                            clearForm();
                            registrationFromActivityListener.loginFragment();
                        }else{
                            showAlertBox(getActivity(),"Sorry! Unable to verify your token");
                        }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showAlertBox(getActivity(),"Sorry! Unable to verify your token at the moment!");
                }
            });

        }else{
            showAlertBox(getActivity(),"Sorry! your verification code doesn't match.");
        }

        return true;
    }


    private void clearForm(){
        token.setText("");
    }


}
