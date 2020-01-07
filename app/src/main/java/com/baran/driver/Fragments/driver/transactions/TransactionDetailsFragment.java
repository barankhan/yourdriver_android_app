package com.baran.driver.Fragments.driver.transactions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.UserRideTransaction;
import com.baran.driver.R;

import java.text.DecimalFormat;


public class TransactionDetailsFragment extends Fragment {

    private TextView tvTransactionId,tvTransactionType,tvDriverStartupFare,tvCompanyServiceCharges,
            tvTimeElapsedMinutes,tvTimeElapsedRate,tvKmTravelled,tvKmTravelledRate,tvTotalFare,tvAmountReceived,tvDistanceAmount,tvTimeAmount,
            tvRideRegisteredAt,tvDriverArrivedAt,tvRideStartedAt,tvRideEndedAt,tvPassengerName,tvTransactionCreatedAt,tvRideRating;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_transaction_details, container, false);

        tvTransactionId = root.findViewById(R.id.tv_transaction_id);
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






        if(getArguments().getInt("transactionId")>0){
            Call <UserRideTransaction> initiateCall = TransactionsFragment.driverTransactionApi.getTransactionDetails(getArguments().getInt("transactionId"));
            Utils.showProgressBarSpinner(getContext());
            initiateCall.enqueue(new Callback<UserRideTransaction>() {
                @Override
                public void onResponse(Call<UserRideTransaction> call, Response<UserRideTransaction> response) {
                    Utils.dismissProgressBarSpinner();
                    if(response.isSuccessful()){
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

                }
            });
        }else{
            getActivity().getFragmentManager().popBackStack();
        }





        return  root;
    }

}
