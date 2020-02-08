package live.yourdriver.driver.Fragments.driver.tickets;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import live.yourdriver.driver.Activity.MainActivity;
import live.yourdriver.driver.Model.DriverServerResponse;
import live.yourdriver.driver.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.Utils;

import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.SupportTicketAPI;


public class NewTicketFragment extends Fragment {

    private EditText etTitle,etMessage;
    private Button btnSubmit;
    private User currentUser;
    public static SupportTicketAPI supportTicketAPI;


    public NewTicketFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        supportTicketAPI = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_SUPPORT_TICKETS).create(SupportTicketAPI.class);
        View root =  inflater.inflate(R.layout.fragment_new_ticket, container, false);

        etTitle = root.findViewById(R.id.et_title);
        etMessage = root.findViewById(R.id.et_message);
        btnSubmit = root.findViewById(R.id.btn_submit_ticket);

        currentUser = MainActivity.appPreference.getUserObject(getContext(),getActivity());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title,message;
                title = etTitle.getText().toString();
                message = etMessage.getText().toString();
                Utils.showProgressBarSpinner(getContext());
                Call<DriverServerResponse> call = supportTicketAPI.createSupportTicket(currentUser.getId(),message,title);
                call.enqueue(new Callback<DriverServerResponse>() {
                    @Override
                    public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {
                        Utils.dismissProgressBarSpinner();
                        if(response.isSuccessful()){
                            etTitle.setText("");
                            etMessage.setText("");
                            Utils.showAlertBox(getActivity(),"Your Issue is reported successfully, Out Agent will get back soon!.");
                        }
                    }

                    @Override
                    public void onFailure(Call<DriverServerResponse> call, Throwable t) {
                        Utils.dismissProgressBarSpinner();
                    }
                });






            }
        });




        return root;
    }


}
