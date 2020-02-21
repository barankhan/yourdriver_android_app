package live.yourdriver.driver.Activity;

import android.media.Ringtone;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import live.yourdriver.driver.Adapters.SupportMessageAdapter;
import live.yourdriver.driver.Constants.Constant;
import live.yourdriver.driver.Extras.AppPreference;
import live.yourdriver.driver.Model.DriverServerResponse;
import live.yourdriver.driver.Model.SupportTicketMessage;
import live.yourdriver.driver.Model.User;
import live.yourdriver.driver.R;;
import live.yourdriver.driver.Services.RetrofitClient;
import live.yourdriver.driver.Services.SupportTicketAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupportActivity extends AppCompatActivity {

    EditText userInput;
    RecyclerView recyclerView;
    SupportMessageAdapter messageAdapter;
    List<SupportTicketMessage> responseMessageList;
    User currentUser;
    int ticketId;
    Button btnSendChat;
    TextView tvSubject;
    public static AppPreference appPreference;

    public static SupportTicketAPI supportTicketAPI;
    private Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        appPreference = new AppPreference(this);

        supportTicketAPI = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_SUPPORT_TICKETS).create(SupportTicketAPI.class);
        currentUser = appPreference.getUserObject(this,this);
        tvSubject = findViewById(R.id.tv_subject);
        Set<String> keys = getIntent().getExtras().keySet();

        if(getIntent().getExtras().containsKey("ticket_id")){
            ticketId = Integer.valueOf(getIntent().getExtras().getString("ticket_id"));
            tvSubject.setText(getIntent().getExtras().getString("subject"));
        }
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        btnSendChat = findViewById(R.id.btn_send_chat);
        btnSendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        responseMessageList = new ArrayList<>();
        messageAdapter = new SupportMessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);


        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    sendMessage();
                }
                return false;
            }
        });


    }

    private void sendMessage(){

        if(userInput.getText().toString().length()>0){
            SupportTicketMessage responseMessage = new SupportTicketMessage(userInput.getText().toString(), currentUser.getId(), currentUser.getId());
            responseMessageList.add(responseMessage);
            messageAdapter.notifyDataSetChanged();

            Call<DriverServerResponse> d = supportTicketAPI.createSupportTicketMessage(currentUser.getId(),userInput.getText().toString(),ticketId);
            d.enqueue(new Callback<DriverServerResponse>() {
                @Override
                public void onResponse(Call<DriverServerResponse> call, Response<DriverServerResponse> response) {

                }

                @Override
                public void onFailure(Call<DriverServerResponse> call, Throwable t) {

                }
            });

            userInput.setText("");
            goToLastRecyclerView();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();


    }


    @Override
    protected void onResume() {
        super.onResume();


        loadMessages();
    }






    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }

    private void loadMessages(){
        Call<List<SupportTicketMessage>> cm = supportTicketAPI.getSupportTicketMessages(currentUser.getId(),ticketId);
        cm.enqueue(new Callback<List<SupportTicketMessage>>() {
            @Override
            public void onResponse(Call<List<SupportTicketMessage>> call, Response<List<SupportTicketMessage>> response) {
                responseMessageList.clear();
                responseMessageList.addAll(response.body());
                messageAdapter.notifyDataSetChanged();
                goToLastRecyclerView();
            }

            @Override
            public void onFailure(Call<List<SupportTicketMessage>> call, Throwable t) {

            }
        });

    }


    private void goToLastRecyclerView(){
        if (!isLastVisible() && messageAdapter.getItemCount()>0)
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
    }
}
