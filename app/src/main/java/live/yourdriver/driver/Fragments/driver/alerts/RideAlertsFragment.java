package live.yourdriver.driver.Fragments.driver.alerts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import live.yourdriver.driver.Adapters.DriverTransactionAdapter;
import live.yourdriver.driver.Adapters.RideAlertAdapter;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.RideAlert;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.RidesApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RideAlertsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private RideAlertAdapter rideAlertAdapter;
    List<RideAlert> rideAlerts = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    public static RidesApi ridesApi;
    private User currentUser;
    private static int page_no=0;
    public static AppPreference appPreference;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.driver_fragment_ride_alerts, container, false);

        appPreference = new AppPreference(getContext());
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
        currentUser = appPreference.getUserObject(getContext(),getActivity());
        recyclerView = (RecyclerView) root.findViewById(R.id.ride_alert_recycler_view);
        manager = new LinearLayoutManager(getContext());
        rideAlertAdapter = new RideAlertAdapter(getContext(), rideAlerts);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(rideAlertAdapter);



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

        if(rideAlerts.size()==0){
            page_no=0;
            getData();
        }


        return root;
    }

    private void getData(){
        Utils.showProgressBarSpinner(getContext());
        Call<List<RideAlert>> initiateCall = ridesApi.getRideAlerts(currentUser.getId(),page_no);
        initiateCall.enqueue(new Callback<List<RideAlert>>() {
            @Override
            public void onResponse(Call<List<RideAlert>> call, Response<List<RideAlert>> response) {
                page_no++;
                Utils.dismissProgressBarSpinner();
                if(response.isSuccessful()){
                    rideAlerts.addAll(response.body());
                    rideAlertAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<RideAlert>> call, Throwable t) {
//                Log.e("error",t.toString());

            }
        });
    }
}