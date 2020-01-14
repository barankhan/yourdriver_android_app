package com.baran.driver.Fragments.passenger.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.baran.driver.Activity.MainActivity;
import com.baran.driver.Adapters.DriverTransactionAdapter;
import com.baran.driver.Constants.Constant;
import com.baran.driver.Extras.Utils;
import com.baran.driver.Model.DriverTransaction;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.DriverTransactionApi;
import com.baran.driver.Services.PassengerTransactionApi;
import com.baran.driver.Services.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private DriverTransactionAdapter rideAdapter;
    List<DriverTransaction> transactions = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    public static PassengerTransactionApi passengerTransactionApi;
    private User currentUser;
    private static int page_no=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.passenger_fragment_transactions, container, false);
        final TextView textView = root.findViewById(R.id.d_text_gallery);
        passengerTransactionApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_DRIVER_TRNSACTIONS).create(PassengerTransactionApi.class);

        currentUser = MainActivity.appPreference.getUserObject(getContext(),getActivity());

        recyclerView = (RecyclerView) root.findViewById(R.id.rides_recycler_view);
        manager = new LinearLayoutManager(getContext());
        rideAdapter = new DriverTransactionAdapter(getContext(), transactions);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(rideAdapter);



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    getData();
                }
            }
        });

        if(transactions.size()==0){
            page_no=0;
            getData();
        }
        return root;
    }


    private void getData(){
        Utils.showProgressBarSpinner(getContext());
        Call<List<DriverTransaction>> initiateCall = passengerTransactionApi.getTransactions(currentUser.getId(),page_no);
        initiateCall.enqueue(new Callback<List<DriverTransaction>>() {
            @Override
            public void onResponse(Call<List<DriverTransaction>> call, Response<List<DriverTransaction>> response) {
                page_no++;
                Utils.dismissProgressBarSpinner();
                if(response.isSuccessful()){
                    transactions.addAll(response.body());
                    rideAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<DriverTransaction>> call, Throwable t) {

            }
        });
    }
}
