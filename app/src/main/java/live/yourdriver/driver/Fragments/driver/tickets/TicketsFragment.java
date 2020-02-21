package live.yourdriver.driver.Fragments.driver.tickets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import live.yourdriver.driver.Adapters.TicketAdapter;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Extras.Utils;
import live.yourdriver.driver.Model.SupportTicket;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.SupportTicketAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TicketsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private TicketAdapter ticketAdapter;
    private FloatingActionButton addTicket;

    List<SupportTicket> supportTickets = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    public static SupportTicketAPI supportTicketAPI;
    private User currentUser;
    private static int page_no=0;
    public static AppPreference appPreference;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        page_no =0;
        appPreference = new AppPreference(getContext());
        supportTickets.clear();
        View root = inflater.inflate(R.layout.driver_fragment_tickets, container, false);

        addTicket = root.findViewById(R.id.fab_add_support_ticket);

        addTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                NewTicketFragment newTicketFragment = new NewTicketFragment();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, newTicketFragment).addToBackStack("@").commit();
            }
        });


        supportTicketAPI = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_SUPPORT_TICKETS).create(SupportTicketAPI.class);

        currentUser = appPreference.getUserObject(getContext(),getActivity());

        recyclerView = (RecyclerView) root.findViewById(R.id.tickets_recycler_view);
        manager = new LinearLayoutManager(getContext());
        ticketAdapter = new TicketAdapter(getContext(), supportTickets);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(ticketAdapter);



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



            getData();



        return root;
    }


    private void getData(){
        Utils.showProgressBarSpinner(getContext());
        Call<List<SupportTicket>> initiateCall = supportTicketAPI.getSupportTickets(currentUser.getId(),page_no);
        initiateCall.enqueue(new Callback<List<SupportTicket>>() {
            @Override
            public void onResponse(Call<List<SupportTicket>> call, Response<List<SupportTicket>> response) {
                page_no++;
                Utils.dismissProgressBarSpinner();
                if(response.isSuccessful()){
                    supportTickets.addAll(response.body());
                    ticketAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<SupportTicket>> call, Throwable t) {
//                Log.e("error",t.toString());

            }
        });
    }
}