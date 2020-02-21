package live.yourdriver.driver.Fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import live.yourdriver.driver.Activity.MainActivity;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.ServiceApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import live.yourdriver.driver.R;;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private EditText etName, etFather,etEmail;
    private Button btnUpdateProfile;
    private User currentUser;

    public static AppPreference appPreference;
    public static ServiceApi serviceApi;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        appPreference = new AppPreference(getContext());
        serviceApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_USERS_API).create(ServiceApi.class);

        currentUser = appPreference.getUserObject(getContext(),getActivity());

        etName = view.findViewById(R.id.et_profile_name);

        etName.setText(currentUser.getName());

        etFather = view.findViewById(R.id.et_profile_father);

        etFather.setText(currentUser.getFather());

        etEmail = view.findViewById(R.id.et_profile_email);

        etEmail.setText(currentUser.getEmail());
        //Log.e("created_at: ", c_date);

        btnUpdateProfile = view.findViewById(R.id.bt_profile_update);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String father = etFather.getText().toString();
                String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(name)){
                    appPreference.showToast("Your name is required.");
                }else if (TextUtils.isEmpty(email)){
                    appPreference.showToast("Your email is required.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    appPreference.showToast("Invalid email");
                }
                else {

                    Call<User> c = serviceApi.updateProfile(currentUser.getMobile(), name, father, email);
                    Utils.showProgressBarSpinner(getContext());
                    c.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Utils.dismissProgressBarSpinner();
                            if (response.isSuccessful()) {
                                appPreference.setUserObject(response.body());
                                Utils.showAlertBox(getActivity(), "Profile updated successfully!");
                            } else {
                                Utils.showAlertBox(getActivity(), "Unable to update data on the server");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Utils.showAlertBox(getActivity(), "Unable to connect to the server");
                        }
                    });

                }
            }
        });

        return view;
    } // ending onCreateView


}
