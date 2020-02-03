package live.yourdriver.driver.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import live.yourdriver.driver.Activity.MainActivity;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    EditText etOldPassword,etNewPassword,etConfirmPassword;
    Button btnUpdatePassword;
    User u;
    String oldPassword,newPassword,confirmPassword;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_change_password, container, false);

        u=MainActivity.appPreference.getUserObject(getContext(),getActivity());
        etOldPassword = root.findViewById(R.id.et_old_password);
        etNewPassword = root.findViewById(R.id.et_new_password);
        etConfirmPassword = root.findViewById(R.id.et_confirm_password);
        btnUpdatePassword = root.findViewById(R.id.btn_update_password);



        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oldPassword= etOldPassword.getText().toString().trim();
                newPassword = etNewPassword.getText().toString().trim();
                confirmPassword = etConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(oldPassword)){
                    MainActivity.appPreference.showToast("Old Password is required");
                } else if (!newPassword.equals(confirmPassword)){
                    MainActivity.appPreference.showToast("Password and Confirm password doesn't match");
                }else if (newPassword.length() < 6){
                    MainActivity.appPreference.showToast("Create a password at least 6 characters long.");
                }else{
                    Utils.showProgressBarSpinner(getContext());

                    Call<User> userCall = MainActivity.serviceApi.updatePassword(u.getMobile(), oldPassword,newPassword);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Utils.dismissProgressBarSpinner();
                            if(response.body().getResponse().equals("success")){
                                Utils.showAlertBox(getActivity(),"Your Password Updated Successfully.");
                            }else if(response.body().getResponse().equals("mismatch")){
                                Utils.showAlertBox(getActivity(),"Your Old Password doesn't match");
                            }else{
                                Utils.showAlertBox(getActivity(),"Something Went Wrong, Please try again later");
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Utils.showAlertBox(getActivity(),"Unable to connect to server!");
                        }
                    });
                }
            }
        });







        return  root;
    }


}
