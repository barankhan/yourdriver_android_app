package live.yourdriver.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.Model.UserTransaction;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.RidesApi;
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

import live.yourdriver.driver.Model.DriverTransaction;

import live.yourdriver.driver.R;;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class DriverTransactionActivity extends AppCompatActivity {

    DriverTransaction driverTransaction;
    TextView tvChange,tvTotalFare;
    EditText etTransAmount;
    Button btnSave;
    public static RidesApi ridesApi;
    public static AppPreference appPreference;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_transaction);
        c = this;


        appPreference = new AppPreference(this);
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);

        tvChange = findViewById(R.id.tv_passenger_change);

        tvTotalFare = findViewById(R.id.tv_payable_amount);

        etTransAmount = findViewById(R.id.et_trans_amount);

        btnSave = findViewById(R.id.btn_save_trans_amount);

        driverTransaction = appPreference.getDriverTransactionObject();

        if(driverTransaction==null){
            Utils.showAlertBox(this,"There is some problem in the transaction!");
        }else{
            tvTotalFare.setText(driverTransaction.getPayableAmount().toString());
        }


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Double.valueOf(etTransAmount.getText().toString())<Math.floor(driverTransaction.getPayableAmount())){
                    Utils.showAlertBox(DriverTransactionActivity.this,"You can't charge less Amount");
                }else{

                    Utils.showProgressBarSpinner(c);
                    User u = appPreference.getUserObject(c,DriverTransactionActivity.this);
                    Call<UserTransaction> userCall = ridesApi.updateTransaction(u.getMobile(),driverTransaction.getId(),etTransAmount.getText().toString());
                    userCall.enqueue(new Callback<UserTransaction>() {
                        @Override
                        public void onResponse(Call<UserTransaction> call, Response<UserTransaction> response) {
                            Utils.dismissProgressBarSpinner();
                            if(response.isSuccessful()){
                                if(response.body().getResponse().equals("amount_update_success")){
                                    appPreference.setUserObject(response.body().getUser());
                                    appPreference.setDriverTransactionObject(null);
                                    finish();
                                }else {
                                    Utils.showAlertBox(DriverTransactionActivity.this,response.body().getMessage());
                                }

                            }else{
                                Utils.showAlertBox(DriverTransactionActivity.this,"Unable to update on server!");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserTransaction> call, Throwable t) {
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

                    DecimalFormat df = new DecimalFormat("#.##");
                    df.setRoundingMode(RoundingMode.FLOOR);
                    double roundOff = enteredAmount- driverTransaction.getPayableAmount();
                    tvChange.setText(df.format(roundOff));
                }else{
                    tvChange.setText("");
                }
            }
        });
    }
}
