package com.baran.driver.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.MyInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static com.baran.driver.Extras.Utils.showAlertBox;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private MyInterface loginFromActivityListener;
    private TextView registerTV,forgetPasswordTV;

    private EditText mobileInput, passwordInput;
    private Button loginBtn;
    private String firebaseToken;
    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (MainActivity.appPreference.getLoginStatus()){
            User u = MainActivity.appPreference.getUserObject();
            loginFromActivityListener.login(u);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                firebaseToken = mToken;

            }
        });




        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        // for login
        mobileInput = view.findViewById(R.id.et_login_mobile);
        passwordInput = view.findViewById(R.id.et_login_pass);
        loginBtn = view.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        registerTV = view.findViewById(R.id.registerTV);
        forgetPasswordTV = view.findViewById(R.id.forgetPasswordTV);

        forgetPasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFromActivityListener.forgetPassword();
            }
        });


        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFromActivityListener.register();
            }
        });
        return view;
    } //ending onCreateView

    private void loginUser() {
        String mobile_number = mobileInput.getText().toString();
        String Password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(mobile_number)){
            MainActivity.appPreference.showToast("Your email is required.");
        } else if (mobile_number.length() != 11) {
            MainActivity.appPreference.showToast("Please Enter Correct Mobile Number");
        } else if (TextUtils.isEmpty(Password)){
            MainActivity.appPreference.showToast("Password required");
        } else if (Password.length() < 6){
            MainActivity.appPreference.showToast("Password  may be at least 6 characters long.");
        } else {
            Call<User> userCall = MainActivity.serviceApi.doLogin(mobile_number, Password,firebaseToken);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.body().getResponse().equals("data")){
                        MainActivity.appPreference.setLoginStatus(true); // set login status in sharedPreference
                        MainActivity.appPreference.setUserObject(response.body());
                        loginFromActivityListener.login(response.body());
                    } else if (response.body().getResponse().equals("login_failed")){
                        MainActivity.appPreference.showToast("Sorry! Your Login information is not correct");
                        passwordInput.setText("");
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    showAlertBox(getActivity(),"Unable to connect to server");
                }
            });
        }
    } //ending loginUser()

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        loginFromActivityListener = (MyInterface) activity;
    }

}
