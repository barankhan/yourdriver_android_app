package live.yourdriver.driver.Fragments.passenger.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.PassengerDashboard;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;
import live.yourdriver.driver.Services.DashboardApi;
import live.yourdriver.driver.Services.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;;

public class DashboardFragment extends Fragment {

    public static AppPreference appPreference;
    public static DashboardApi dashboardApi;
    private User currentUser;

    private ImageView ImFbIcon,ImWebIcon;

    private TextView tvPBalance,tvPReferredUsers,tvPReferredDrivers,
            tvPReferralCode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.passenger_fragment_dashboard, container, false);

        appPreference = new AppPreference(getContext());
        dashboardApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_USERS_API).create(DashboardApi.class);
        currentUser = appPreference.getUserObject(getContext(),getActivity());


        tvPBalance = root.findViewById(R.id.tvPBalance);
        tvPReferredUsers = root.findViewById(R.id.tvPReferredPassenger);
        tvPReferredDrivers = root.findViewById(R.id.tvPReferredDriver);
        tvPReferralCode = root.findViewById(R.id.tvPRefarralCode);



        ImFbIcon = root.findViewById(R.id.im_fb_icon);
        ImWebIcon = root.findViewById(R.id.im_web_icon);

        ImFbIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/yourdriver.live/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ImWebIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://yourdriver.live/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



        Utils.showProgressBarSpinner(getContext());
        Call<PassengerDashboard> initiateCall = dashboardApi.getPassengerData(currentUser.getId());
        initiateCall.enqueue(new Callback<PassengerDashboard>() {
            @Override
            public void onResponse(Call<PassengerDashboard> call, Response<PassengerDashboard> response) {
                Utils.dismissProgressBarSpinner();
                if(response.isSuccessful()){
                    tvPBalance.setText(response.body().getBalance());
                    tvPReferralCode.setText(response.body().getReferralCode());
                    tvPReferredDrivers.setText(response.body().getReferredDrivers());
                    tvPReferredUsers.setText(response.body().getReferredUsers());
                }
            }

            @Override
            public void onFailure(Call<PassengerDashboard> call, Throwable t) {
                Utils.dismissProgressBarSpinner();

            }
        });




        return root;
    }
}