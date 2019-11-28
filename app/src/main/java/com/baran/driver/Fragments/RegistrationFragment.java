package com.baran.driver.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.MyInterface;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private MyInterface registrationFromActivityListener;


    private EditText nameInput, emailInput, phoneInput, passwordInput;
    Button regBtn;

    private String firebaseToken;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_registration, container, false);
        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.et_login_mobile);
        phoneInput = view.findViewById(R.id.phoneInput);
        passwordInput = view.findViewById(R.id.et_login_pass);
        regBtn = view.findViewById(R.id.regBtn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(),  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String mToken = instanceIdResult.getToken();
                firebaseToken = mToken;

            }
        });


        return view;
    }

    public void registerUser() {

        final String name = nameInput.getText().toString();
        final String email = emailInput.getText().toString();
        final String phone = phoneInput.getText().toString();
        final String password = passwordInput.getText().toString();

        if (TextUtils.isEmpty(name)){
            MainActivity.appPreference.showToast("Your name is required.");
        }else if (TextUtils.isEmpty(phone)){
            MainActivity.appPreference.showToast("Your Mobile Number is required.");
        } else if (TextUtils.isEmpty(email)){
            MainActivity.appPreference.showToast("Your email is required.");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            MainActivity.appPreference.showToast("Invalid email");
        } else if (TextUtils.isEmpty(password)){
            MainActivity.appPreference.showToast("Password required");
        } else if (password.length() < 6){
            MainActivity.appPreference.showToast("Create a password at least 6 characters long.");
        }else if (phone.length() != 11){
            MainActivity.appPreference.showToast("Please Enter Correct Mobile Number");
        }
        else {

            Call<User> userCall = MainActivity.serviceApi.doRegistration(name, email, phone, password,firebaseToken);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        Log.e("Token Called","Called");
                        if(response.body().getResponse().equals("inserted")){
                            MainActivity.appPreference.setUserObject(response.body());
                            clearForm();
                            registrationFromActivityListener.registrationVerificationFragment();
                        }else if(response.body().getResponse().equals("exists")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Your Mobile Number already Register. Do You Want to Login")
                                    // Set the action buttons
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            registrationFromActivityListener.loginFragment();
                                            clearForm();
                                        }
                                    }).setNegativeButton(R.string.no, null).create().show();


                        } else if(response.body().getResponse().equals("error")){
                            MainActivity.appPreference.showToast("Oops! something went wrong.");
                        }
                    }else{

                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("FIX",t.toString());
                }
            });





//            userCall.enqueue(new SortedList.Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//
//                    System.out.println(response.body().getResponse());
//                    Log.e("response_body",response.body().toString());
//                    if (response.body().getResponse().equals("inserted")){
//                        Log.e("response", response.body().getResponse());
//                        Log.e("status_header",response.headers().get("CustomStatus"));
//                        nameInput.setText("");
//                        emailInput.setText("");
//                        phoneInput.setText("");
//                        passwordInput.setText("");
//                        MainActivity.appPreference.showToast("Registered Successfully");
//                    } else if (response.body().getResponse().equals("exists")){
//                        MainActivity.appPreference.showToast("This email already exists");
//                    } else if (response.body().getResponse().equals("error")){
//                        MainActivity.appPreference.showToast("Oops! something went wrong.");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//                }
//            });
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        registrationFromActivityListener = (MyInterface) activity;
    }


    private void clearForm(){
        nameInput.setText("");
        emailInput.setText("");
        phoneInput.setText("");
        passwordInput.setText("");
    }

}
