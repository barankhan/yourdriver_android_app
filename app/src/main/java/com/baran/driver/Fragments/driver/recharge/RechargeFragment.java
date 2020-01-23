package com.baran.driver.Fragments.driver.recharge;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Constants.Constant;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.DriverTransactionApi;
import com.baran.driver.Services.RetrofitClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RechargeFragment extends Fragment {

    private Button btnRecharge;
    private EditText etTransactionId,etAmount;
    private Spinner spPaymentTypes;
    public static DriverTransactionApi driverTransactionApi;
    private User currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.driver_fragment_recharge, container, false);
        driverTransactionApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_DRIVER_TRNSACTIONS).create(DriverTransactionApi.class);

        btnRecharge = root.findViewById(R.id.btn_recharge);
        etTransactionId = root.findViewById(R.id.et_transaction_id);
        etAmount = root.findViewById(R.id.et_amount);
        spPaymentTypes = root.findViewById(R.id.sp_payment_type);
        currentUser= MainActivity.appPreference.getUserObject(getContext(),getActivity());
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showProgressBarSpinner(getContext());
                Call<User> call = driverTransactionApi.recharge(currentUser.getId(),spPaymentTypes.getSelectedItem().toString(),etTransactionId.getText().toString(),Double.valueOf(etAmount.getText().toString()));
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Utils.dismissProgressBarSpinner();
                        if(response.isSuccessful()){
                            MainActivity.appPreference.setUserObject(response.body());
                            if(response.body().getResponse().equals("payment_successful")){
                                Utils.showAlertBox(getActivity(),"Your Account has been recharged successfully");
                            }else if (response.body().getResponse().equals("voucher_not_found")){
                                Utils.showAlertBox(getActivity(),"Your request is under process, Please verify transaction data");
                            }else if (response.body().getResponse().equals("already_used")){
                                Utils.showAlertBox(getActivity(),"The transaction information is already used.");
                            }else if (response.body().getResponse().equals("invalid_request")){
                                Utils.showAlertBox(getActivity(),"Invalid Transaction Information!. Contact Support.");
                            }else{
                                Utils.showAlertBox(getActivity(),"Error!");
                            }
                        }else{
                            Utils.showAlertBox(getActivity(),"There is some error.");
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                        Log.e("ERROR",t.toString());
                        Utils.showAlertBox(getActivity(),"Unable to connect to the server!.");
                    }
                });

            }
        });
        return root;
    }
}