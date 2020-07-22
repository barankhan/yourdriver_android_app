package live.yourdriver.driver.Fragments.driver.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.Dashboard;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import live.yourdriver.driver.Services.DashboardApi;
import live.yourdriver.driver.Services.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment {

    private TextView tvBalance,tvLast7DaysEarning,tvLast30DaysEarning,tvReferredUsers,tvReferredDrivers,
            tvReferralCode,tvDashTodayEarning,tvVehicleType;

    public static AppPreference appPreference;
    public static DashboardApi dashboardApi;
    private User currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.driver_fragment_share, container, false);

        appPreference = new AppPreference(getContext());
        dashboardApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_USERS_API).create(DashboardApi.class);
        currentUser = appPreference.getUserObject(getContext(),getActivity());

        tvBalance = root.findViewById(R.id.tv_dash_balance);
        tvLast7DaysEarning = root.findViewById(R.id.tv_last_7_days_earning);
        tvLast30DaysEarning = root.findViewById(R.id.tv_last_30_days_earning);
        tvReferredUsers = root.findViewById(R.id.tv_referred_users);
        tvReferredDrivers = root.findViewById(R.id.tv_referred_drivers);
        tvReferralCode = root.findViewById(R.id.tv_referral_code);
        tvDashTodayEarning = root.findViewById(R.id.tv_dash_today_earning);
        tvVehicleType = root.findViewById(R.id.tv_vehicle_type);



        Utils.showProgressBarSpinner(getContext());
        Call<Dashboard> initiateCall = dashboardApi.getData(currentUser.getId());
        initiateCall.enqueue(new Callback<Dashboard>() {
            @Override
            public void onResponse(Call<Dashboard> call, Response<Dashboard> response) {
                Utils.dismissProgressBarSpinner();
                if(response.isSuccessful()){
                    tvBalance.setText(response.body().getBalance());
                    tvLast7DaysEarning.setText(response.body().getLast7DaysEarning());
                    tvLast30DaysEarning.setText(response.body().getLast30DaysEarning());
                    tvDashTodayEarning.setText(response.body().getDashTodayEarning());
                    tvReferralCode.setText(response.body().getReferralCode());
                    tvReferredDrivers.setText(response.body().getReferredDrivers());
                    tvReferredUsers.setText(response.body().getReferredUsers());
                    tvVehicleType.setText(response.body().getVehicleType());
                }
            }

            @Override
            public void onFailure(Call<Dashboard> call, Throwable t) {
                Utils.dismissProgressBarSpinner();

            }
        });






        return root;
    }
}