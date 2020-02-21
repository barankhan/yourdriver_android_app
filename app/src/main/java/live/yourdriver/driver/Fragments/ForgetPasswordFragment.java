package live.yourdriver.driver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import live.yourdriver.driver.Activity.MainActivity;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.DriverServerResponse;

import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.MyInterface;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordFragment extends Fragment {

    Button forgotPasswordButton;
    EditText token;
    private MyInterface registrationFromActivityListener;
    public static AppPreference appPreference;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appPreference = new AppPreference(getContext());

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        forgotPasswordButton = view.findViewById(R.id.bt_forgot_password);
        token = view.findViewById(R.id.et_forgot_password);




        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(token.getText().length()!=11)
                        appPreference.showToast("Please Enter Valid Mobile Number");
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

        String Mobi = token.getText().toString().trim();

        if (Mobi.length() != 11) {
            appPreference.showToast("Please Enter Correct Mobile Number");
        } else {
            Utils.showProgressBarSpinner(getContext());
            Call<DriverServerResponse> userCall = MainActivity.serviceApi.doSendPasswordViaSMS(Mobi);
            userCall.enqueue(new Callback<DriverServerResponse>() {
                @Override
                public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                    Utils.dismissProgressBarSpinner();
                    if (response.body().getResponse().equals("sms_sent")) {
                        appPreference.showToast("Please check your SMS");
                        registrationFromActivityListener.loginFragment();

                    } else {
                        Utils.showAlertBox(getActivity(), "Your mobile number is not registered with us.");
                    }
                }

                @Override
                public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                    Utils.dismissProgressBarSpinner();
                    appPreference.showToast("There is some error We can't communicate with Server");
                }
            });


        }

    }


    private void clearForm(){
        token.setText("");
    }


}
