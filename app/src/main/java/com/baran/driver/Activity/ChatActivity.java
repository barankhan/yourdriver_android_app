package com.baran.driver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.baran.driver.Adapters.ChatMessageAdapter;
import com.baran.driver.Constants.Constant;
import com.baran.driver.Model.ChatMessage;
import com.baran.driver.Model.DriverServerResponse;
import com.baran.driver.Model.User;
import com.baran.driver.R;
import com.baran.driver.Services.RetrofitClient;
import com.baran.driver.Services.RidesApi;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatActivity extends AppCompatActivity {

    EditText userInput;
    RecyclerView recyclerView;
    ChatMessageAdapter messageAdapter;
    List<ChatMessage> responseMessageList;
    User currentUser;
    int rideId;
    public static boolean inFront = false;
    public static RidesApi ridesApi;
    private Ringtone r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ridesApi = RetrofitClient.getApiClient(Constant.baseUrl.BASE_URL_RIDES_API).create(RidesApi.class);
        currentUser = MainActivity.appPreference.getUserObject(this,this);
        Set<String> keys = getIntent().getExtras().keySet();

        if(getIntent().getExtras().containsKey("ride_id")){
            rideId = Integer.valueOf(getIntent().getExtras().getString("ride_id"));
        }
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new ChatMessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        if(getIntent().getExtras().containsKey("new_message_received")) {
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    ChatMessage responseMessage = new ChatMessage(userInput.getText().toString(), currentUser.getId(), currentUser.getId());
                    responseMessageList.add(responseMessage);
                    messageAdapter.notifyDataSetChanged();

                    Call<DriverServerResponse> d = ridesApi.insertChat(currentUser.getId(),userInput.getText().toString(),rideId);
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
                return false;
            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        inFront = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mChatMessageReceived);
    }


    @Override
    protected void onResume() {
        super.onResume();
        inFront = true;
        LocalBroadcastManager.getInstance(this).registerReceiver((mChatMessageReceived),
                new IntentFilter("NewChatMessageReceived")
        );
        loadMessages();
    }




    private BroadcastReceiver mChatMessageReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ChatMessage c = new ChatMessage(intent.getStringExtra("message"),intent.getIntExtra("sender_id",0),currentUser.getId());
            responseMessageList.add(c);
            messageAdapter.notifyDataSetChanged();
            goToLastRecyclerView();
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                r.play();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = recyclerView.getAdapter().getItemCount();
        return (pos >= numItems);
    }

    private void loadMessages(){
        Call<List<ChatMessage>> cm = ridesApi.getChat(currentUser.getId(),rideId);
        cm.enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                responseMessageList.addAll(response.body());
                messageAdapter.notifyDataSetChanged();
                goToLastRecyclerView();
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {

            }
        });

    }


    private void goToLastRecyclerView(){
        if (!isLastVisible() && messageAdapter.getItemCount()>0)
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
    }
}