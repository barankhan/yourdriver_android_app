package com.baran.driver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.MyInterface;

import androidx.fragment.app.Fragment;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.baran.driver.Extras.Utils.showAlertBox;

public class ForgetPasswordFragment extends Fragment {

    Button forgotPasswordButton;
    EditText token;
    User u;
    private MyInterface registrationFromActivityListener;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        u = MainActivity.appPreference.getUserObject();
        forgotPasswordButton = view.findViewById(R.id.bt_forgot_password);
        token = view.findViewById(R.id.et_forgot_password);




        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(token.getText().length()!=11)
                        MainActivity.appPreference.showToast("Please Enter Valid Mobile Number");
                    else
                        getPassword();
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


    private void getPassword(){

        String Mobi = token.getText().toString();



            Call<DriverServerResponse> userCall = MainActivity.serviceApi.doSendPasswordViaSMS(Mobi);
            userCall.enqueue(new Callback<DriverServerResponse>() {
                @Override
                public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                        if(response.body().getResponse().equals("sms_sent")){
                            MainActivity.appPreference.showToast("Please check your SMS");
                            registrationFromActivityListener.loginFragment();

                        }else{
                            showAlertBox(getActivity(),"Your mobile number is not registered with us.");
                        }
                }
                @Override
                public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                    MainActivity.appPreference.showToast("There is some error We can't communicate with Server");
                }
            });




    }


    private void clearForm(){
        token.setText("");
    }


}
