package com.baran.driver.Fragments.driver.transactions;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baran.driver.Activity.DriverTransactionActivity;
import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.TransactionLiability;
import com.baran.driver.Model.UserRideTransaction;
import com.baran.driver.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class TransactionDetailsFragment extends Fragment {

    private TextView tvTransactionId,tvTransactionType,tvDriverStartupFare,tvCompanyServiceCharges,
            tvTimeElapsedMinutes,tvTimeElapsedRate,tvKmTravelled,tvKmTravelledRate,tvTotalFare,tvAmountReceived,tvDistanceAmount,tvTimeAmount,
            tvRideRegisteredAt,tvDriverArrivedAt,tvRideStartedAt,tvRideEndedAt,tvPassengerName,tvTransactionCreatedAt,tvRideRating,
            tvCompanyInwardHead,tvInwardHeadAmount,tvCompanyOutwardHead,tvOutwardHeadAmount;

    private Button btnEnterCash;
    private LinearLayout linearLayoutLiabilities;

    private List<TransactionLiability> transactionLiabilityList = new ArrayList<TransactionLiability>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_transaction_details, container, false);

        linearLayoutLiabilities = root.findViewById(R.id.linearLayoutLiabilities);

        btnEnterCash = root.findViewById(R.id.btn_enter_cash);


        tvTransactionId = root.findViewById(R.id.et_transaction_id);
        tvTransactionType = root.findViewById(R.id.tv_transaction_type);

        tvDriverStartupFare = root.findViewById(R.id.tv_driver_startup_fare);
        tvCompanyServiceCharges = root.findViewById(R.id.tv_company_service_charges);

        tvTimeElapsedMinutes = root.findViewById(R.id.tv_time_elapsed_minutes);
        tvTimeElapsedRate = root.findViewById(R.id.tv_time_elapsed_rate);
        tvTimeAmount = root.findViewById(R.id.tv_time_amount);

        tvKmTravelled = root.findViewById(R.id.tv_km_travelled);
        tvKmTravelledRate = root.findViewById(R.id.tv_km_travelled_rate);
        tvDistanceAmount = root.findViewById(R.id.tv_distance_amount);

        tvTotalFare = root.findViewById(R.id.tv_total_fare);
        tvAmountReceived = root.findViewById(R.id.tv_amount_received);
        tvTransactionCreatedAt = root.findViewById(R.id.tv_transaction_created_at);


        tvRideRegisteredAt = root.findViewById(R.id.tv_ride_created_at);
        tvDriverArrivedAt = root.findViewById(R.id.tv_driver_arrived_at);
        tvRideStartedAt = root.findViewById(R.id.tv_ride_started_at);
        tvRideEndedAt = root.findViewById(R.id.tv_ride_ended_at);
        tvRideRating = root.findViewById(R.id.tv_ride_rating);


        tvPassengerName = root.findViewById(R.id.tv_passenger_name);


//        tvCompanyInwardHead = root.findViewById(R.id.tv_company_inward_head);
//        tvCompanyOutwardHead = root.findViewById(R.id.tv_company_outward_head);
//
//        tvInwardHeadAmount = root.findViewById(R.id.tv_inward_head_amount);
//        tvOutwardHeadAmount = root.findViewById(R.id.tv_outward_head_amount);








        if(getArguments().getInt("transactionId")>0){
            Call <UserRideTransaction> initiateCall = TransactionsFragment.driverTransactionApi.getTransactionDetails(getArguments().getInt("transactionId"));
            Utils.showProgressBarSpinner(getContext());
            initiateCall.enqueue(new Callback<UserRideTransaction>() {
                @Override
                public void onResponse(Call<UserRideTransaction> call, Response<UserRideTransaction> response) {
                    Utils.dismissProgressBarSpinner();
                    if(response.isSuccessful()){
                        if(response.body().getDriverTransaction().getTransactionCompleted()==0){
                            MainActivity.appPreference.setDriverTransactionObject(response.body().getDriverTransaction());
                            btnEnterCash.setVisibility(View.VISIBLE);
                        }

                        transactionLiabilityList = response.body().getTransactionLiabilityList();

                        tvTransactionId.setText(String.valueOf(response.body().getDriverTransaction().getId()));
                        tvTransactionType.setText(String.valueOf(response.body().getDriverTransaction().getTransactionType()));

                        tvDriverStartupFare.setText(String.valueOf(response.body().getDriverTransaction().getDriverStartUpFare()));
                        tvCompanyServiceCharges.setText(String.valueOf(response.body().getDriverTransaction().getCompanyServiceCharges()));

                        tvTimeElapsedMinutes.setText(String.valueOf(response.body().getDriverTransaction().getTimeElapsedMinutes()));
                        tvTimeElapsedRate.setText(String.valueOf(response.body().getDriverTransaction().getTimeElapsedRate()));

                        double ta = response.body().getDriverTransaction().getTimeElapsedMinutes()*response.body().getDriverTransaction().getTimeElapsedRate();

                        DecimalFormat df = new DecimalFormat("#.##");

                        tvTimeAmount.setText(String.valueOf(Double.valueOf(df.format(ta))));

                        tvKmTravelled.setText(String.valueOf(response.body().getDriverTransaction().getKmTravelled()));
                        tvKmTravelledRate.setText(String.valueOf(response.body().getDriverTransaction().getKmTravelledRate()));

                        double ka = response.body().getDriverTransaction().getKmTravelled()*response.body().getDriverTransaction().getKmTravelledRate();
                        tvDistanceAmount.setText(String.valueOf(Double.valueOf(df.format(ka))));

                        tvTotalFare.setText(String.valueOf(response.body().getDriverTransaction().getTotalFare()));

//                        if(response.body().getDriverTransaction().getCompanyInwardHead()!=null){
//                            tvCompanyInwardHead.setText(response.body().getDriverTransaction().getCompanyInwardHead());
//                            tvInwardHeadAmount.setText(String.valueOf(response.body().getDriverTransaction().getInwardHeadAmount()));
//
//                            tvCompanyInwardHead.setVisibility(View.VISIBLE);
//                            tvInwardHeadAmount.setVisibility(View.VISIBLE);
//                        }
//
//
//                        if(response.body().getDriverTransaction().getCompanyOutwardHead()!=null){
//                            tvCompanyOutwardHead.setText(response.body().getDriverTransaction().getCompanyOutwardHead());
//                            tvOutwardHeadAmount.setText(String.valueOf(response.body().getDriverTransaction().getOutwardHeadAmount()));
//
//                            tvCompanyOutwardHead.setVisibility(View.VISIBLE);
//                            tvOutwardHeadAmount.setVisibility(View.VISIBLE);
//                        }




                        for (int i = 0; i < transactionLiabilityList.size(); i++) {

                            LinearLayout layout2 = new LinearLayout(getContext());
                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            layout2.setOrientation(LinearLayout.HORIZONTAL);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                            params.setMargins(0, 8, 0, 0);
                            linearLayoutLiabilities.addView(layout2);
                            TextView tvTitle =  new TextView(getContext());
                            tvTitle.setText(transactionLiabilityList.get(i).getTitle());
                            tvTitle.setLayoutParams(params);
                            tvTitle.setGravity(Gravity.LEFT);



                            TextView tvAmount =  new TextView(getContext());
                            tvAmount.setText(String.valueOf(transactionLiabilityList.get(i).getAmount()));
                            tvAmount.setLayoutParams(params);
                            tvAmount.setGravity(Gravity.RIGHT);

                            layout2.addView(tvTitle);
                            layout2.addView(tvAmount);

                        }





                        tvAmountReceived.setText(String.valueOf(response.body().getDriverTransaction().getAmountReceived()));

                        tvTransactionCreatedAt.setText(response.body().getDriverTransaction().getCreatedAt());

                        tvRideRegisteredAt.setText(response.body().getRide().getCreatedAt());
                        tvDriverArrivedAt.setText(response.body().getRide().getDriverArrivedAt());
                        tvRideStartedAt.setText(response.body().getRide().getRideStartedAt());
                        tvRideEndedAt.setText(response.body().getRide().getRideEndedAt());
                        tvRideRating.setText(String.valueOf(response.body().getRide().getRating()));


                        tvPassengerName.setText(response.body().getUser().getName());
                    }
                }

                @Override
                public void onFailure(Call<UserRideTransaction> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                        Log.e("error",t.toString());
                }
            });
        }else{
            getActivity().getFragmentManager().popBackStack();
        }



        btnEnterCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getContext(), DriverTransactionActivity.class);
                startActivity(intent);
            }
        });




        return  root;
    }

}
