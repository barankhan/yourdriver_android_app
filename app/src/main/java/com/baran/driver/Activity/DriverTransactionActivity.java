package com.baran.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baran.driver.Constants.Constant;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.DriverTransaction;
import com.baran.driver.Model.Ride;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.RetrofitClient;
import com.baran.driver.Services.RidesApi;


public class DriverTransactionActivity extends AppCompatActivity {

    DriverTransaction driverTransaction;
    TextView tvChange,tvTotalFare;
    EditText etTransAmount;
    Button btnSave;
    public static RidesApi ridesApi;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_transaction);
        c = this;
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);

        tvChange = findViewById(R.id.tv_passenger_change);
        tvTotalFare = findViewById(R.id.tv_total_fare_amount);

        etTransAmount = findViewById(R.id.et_trans_amount);

        btnSave = findViewById(R.id.btn_save_trans_amount);

        driverTransaction = MainActivity.appPreference.getDriverTransactionObject();

        if(driverTransaction==null){
            Utils.showAlertBox(this,"There is some problem in the transaction!");
        }else{
            tvTotalFare.setText(driverTransaction.getTotalFare().toString());
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Double.valueOf(etTransAmount.getText().toString())> Math.floor(driverTransaction.getTotalFare())){
                    Utils.showAlertBox(DriverTransactionActivity.this,"You can't charge extra Amount");
                }else if(Double.valueOf(etTransAmount.getText().toString())<Math.floor(driverTransaction.getTotalFare())){
                    Utils.showAlertBox(DriverTransactionActivity.this,"You can't charge less Amount");
                }else{

                    Utils.showProgressBarSpinner(c);
                    User u = MainActivity.appPreference.getUserObject(c,DriverTransactionActivity.this);
                    Call<DriverServerResponse> userCall = ridesApi.updateTransaction(u.getMobile(),driverTransaction.getId(),etTransAmount.getText().toString());
                    userCall.enqueue(new Callback<DriverServerResponse>() {
                        @Override
                        public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                            Utils.dismissProgressBarSpinner();
                            if(response.isSuccessful()){

                                User u = MainActivity.appPreference.getUserObjectWithoutUserValidation();
                                u.setBalance(driverTransaction.getDriverBalance());
                                MainActivity.appPreference.setUserObject(u);

                                MainActivity.appPreference.setDriverTransactionObject(null);
                                finish();

                            }else{
                                Utils.showAlertBox(DriverTransactionActivity.this,"Unable to update on server!");
                            }
                        }

                        @Override
                        public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                            Utils.showAlertBox(DriverTransactionActivity.this,"Unable to connect to the server!");
                        }
                    });

                }
            }
        });




        etTransAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length()>1) {
                    Double enteredAmount = Double.valueOf(etTransAmount.getText().toString());
                    tvChange.setText(String.format(String.valueOf(enteredAmount - driverTransaction.getTotalFare()),"$%.2f"));
                }
            }
        });
    }
}
