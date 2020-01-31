package live.yourdriver.driver.Fragments.passenger.transactions;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.TransactionLiability;
import live.yourdriver.driver.Model.UserRideTransaction;
import live.yourdriver.driver.R;;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TransactionDetailsFragment extends Fragment {

    private RatingBar ratingBar;
    private TextView tvTransactionId,tvTransactionType,tvDriverStartupFare,tvCompanyServiceCharges,
            tvTimeElapsedMinutes,tvTimeElapsedRate,tvKmTravelled,tvKmTravelledRate,tvTotalFare,tvAmountReceived,tvDistanceAmount,tvTimeAmount,
            tvRideRegisteredAt,tvDriverArrivedAt,tvRideStartedAt,tvRideEndedAt,tvPassengerName,tvTransactionCreatedAt,tvRideRating,
            tvVehicleType,tvVehicleNo,tvVehicleMade,tvPickupAddress,tvDropoffAddress,tvPickupAddressLabel,tvDropoffAddressLabel


            ;
    private LinearLayout linearLayoutLiabilities;

    private List<TransactionLiability> transactionLiabilityList = new ArrayList<TransactionLiability>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_passenger_transaction_details, container, false);

        ratingBar = root.findViewById(R.id.ratingBar);

        linearLayoutLiabilities = root.findViewById(R.id.linearLayoutLiabilities);

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

        tvPickupAddress = root.findViewById(R.id.tv_pickup_address);
        tvPickupAddressLabel = root.findViewById(R.id.tv_pickup_address_label);

        tvDropoffAddress = root.findViewById(R.id.tv_dropoff_address);
        tvDropoffAddressLabel = root.findViewById(R.id.tv_dropoff_address_label);

        tvPassengerName = root.findViewById(R.id.tv_passenger_name);
        tvVehicleType = root.findViewById(R.id.tv_vehicle_type);
        tvVehicleNo = root.findViewById(R.id.tv_vehicle_no);
        tvVehicleMade = root.findViewById(R.id.tv_vehicle_made);





        if(getArguments().getInt("transactionId")>0){
            Call <UserRideTransaction> initiateCall = TransactionsFragment.passengerTransactionApi.getTransactionDetails(getArguments().getInt("transactionId"));
            Utils.showProgressBarSpinner(getContext());
            initiateCall.enqueue(new Callback<UserRideTransaction>() {
                @Override
                public void onResponse(Call<UserRideTransaction> call, Response<UserRideTransaction> response) {
                    Utils.dismissProgressBarSpinner();
                    if(response.isSuccessful()){

                        transactionLiabilityList = response.body().getTransactionLiabilityList();

                        tvTransactionId.setText(String.valueOf(response.body().getDriverTransaction().getId()));
                        tvTransactionType.setText(String.valueOf(response.body().getDriverTransaction().getTransactionType()));

                        tvDriverStartupFare.setText(String.valueOf(response.body().getDriverTransaction().getDriverStartUpFare()
                        + response.body().getDriverTransaction().getCompanyServiceCharges()
                        ));


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
                        ratingBar.setRating(response.body().getRide().getRating());
//                        tvRideRating.setText(String.valueOf(response.body().getRide().getRating()));

                        if(response.body().getRide().getPickupAddress()!=null){
                            tvPickupAddressLabel.setVisibility(View.VISIBLE);
                            tvPickupAddress.setText(response.body().getRide().getPickupAddress());
                            tvPickupAddress.setVisibility(View.VISIBLE);
                        }

                        if(response.body().getRide().getDropoffAddress()!=null){
                            tvDropoffAddressLabel.setVisibility(View.VISIBLE);
                            tvDropoffAddress.setText(response.body().getRide().getDropoffAddress());
                            tvDropoffAddress.setVisibility(View.VISIBLE);
                        }


                        tvPassengerName.setText(response.body().getUser().getName());
                        tvVehicleType.setText(response.body().getRide().getVehicleType());
                        tvVehicleNo.setText(response.body().getUser().getRegAlphabet()+"-"+
                                response.body().getUser().getRegYear()+"-"+response.body().getUser().getRegNo());
                        tvVehicleMade.setText(response.body().getUser().getVehicleMade()+"-"+response.body().getUser().getVehicleColor());
                    }
                }

                @Override
                public void onFailure(Call<UserRideTransaction> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
//                        Log.e("error",t.toString());
                }
            });
        }else{
            getActivity().getFragmentManager().popBackStack();
        }





        return  root;
    }

}
