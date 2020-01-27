package live.yourdriver.driver.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import live.yourdriver.driver.Activity.MainActivity;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.MyInterface;

public class RegistrationVerificationFragment extends Fragment {

    Button verificationButton,btnResendSms;
    EditText token;
    User u;
    TextView tvVerificationTimer;
    private MyInterface registrationFromActivityListener;

    public RegistrationVerificationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_verification, container, false);
        u = MainActivity.appPreference.getUserObject(getContext(),getActivity());

        verificationButton = view.findViewById(R.id.verificationButton);
        tvVerificationTimer = view.findViewById(R.id.tv_verification_timer);
        btnResendSms = view.findViewById(R.id.btn_resend_sms);

        runCountDown();




        btnResendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showProgressBarSpinner(getContext());
                final Call<User> userCall = MainActivity.serviceApi.resendOTP(u.getMobile());
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Utils.dismissProgressBarSpinner();
                        runCountDown();
                        MainActivity.appPreference.setUserObject(response.body());
                        clearForm();
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();

                    }
                });


            }
        });



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


    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        registrationFromActivityListener = (MyInterface) activity;

    }


    private boolean verifyToken(){

        int tokenValue = Integer.valueOf(token.getText().toString());


        if(u.getVerificationToken()==tokenValue){
            Utils.showProgressBarSpinner(getContext());
            Call<User> userCall = MainActivity.serviceApi.doTokenVerification(u.getMobile(),tokenValue);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Utils.dismissProgressBarSpinner();
                        if(response.body().getResponse().equals("verified")){
                            MainActivity.appPreference.setUserObject(response.body());
                            MainActivity.appPreference.setLoginStatus(true);
                            clearForm();
                            registrationFromActivityListener.loginFragment();
                        }else{
                            Utils.showAlertBox(getActivity(),"Sorry! Unable to verify your token");
                        }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Utils.dismissProgressBarSpinner();
                    Utils.showAlertBox(getActivity(),"Sorry! Unable to verify your token at the moment!");
                }
            });

        }else{
            Utils.showAlertBox(getActivity(),"Sorry! your verification code doesn't match.");
        }

        return true;
    }


    private void clearForm(){
        token.setText("");
    }

    private void runCountDown(){
        btnResendSms.setVisibility(View.GONE);
        final int[] time = {90};
        new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvVerificationTimer.setText("0:"+checkDigit(time[0]));
                time[0]--;
            }

            public void onFinish() {
                btnResendSms.setVisibility(View.VISIBLE);
            }

        }.start();
    }


}
