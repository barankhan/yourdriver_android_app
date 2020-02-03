package live.yourdriver.driver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import live.yourdriver.driver.Model.DBHelper;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import live.yourdriver.driver.Extras.LocationDataAdapter;
import live.yourdriver.driver.Extras.SavedLocationData;
import live.yourdriver.driver.Extras.SavedLocationDataAdapter;
import live.yourdriver.driver.R;;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;

import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;

import com.google.android.libraries.places.api.net.PlacesClient;



import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity  {

    EditText searchLocation;
    AutocompleteSessionToken token;
    FindAutocompletePredictionsRequest request;
    LinearLayout linearLayout;
    DBHelper mydb;
    PlacesClient placesClient;
    ProgressBar pbSearch;

    public static boolean generateNewToken=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_search);
        mydb = new DBHelper(this);
        List<SavedLocationData> array_list = mydb.getAllAddress();
        pbSearch = findViewById(R.id.pb_search);

        ArrayList<SavedLocationData> arrayOfUsers = new ArrayList<SavedLocationData>();
        ArrayList<SavedLocationData> arrayOfSuggestions = new ArrayList<SavedLocationData>();
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyCMeNFGFvsheMOXo7gcJMMiLStrKyHGAFI");
        }
        placesClient = Places.createClient(SearchActivity.this);

        SavedLocationDataAdapter adapter = new SavedLocationDataAdapter(this, arrayOfUsers,SearchActivity.this);
        token = AutocompleteSessionToken.newInstance();
        final LocationDataAdapter locationAdapter = new LocationDataAdapter(this, arrayOfSuggestions,placesClient,SearchActivity.this,adapter);


        ListView listView = (ListView) findViewById(R.id.lvItems);
        if(array_list.size()>0) {

            TextView textView = new TextView(getBaseContext());
            textView.setText("Saved Locations");
            textView.setPadding(15, 0, 0, 0);
            textView.setBackgroundColor(getResources().getColor(R.color.quantum_grey));

            listView.addHeaderView(textView);
        }
        listView.setAdapter(adapter);

        ListView listViewSuggestion = (ListView) findViewById(R.id.lvSuggestions);
        listViewSuggestion.setAdapter(locationAdapter);


        for (int i=0;i<array_list.size();i++) {
            adapter.add(array_list.get(i));
        }



        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        linearLayout = findViewById(R.id.address_linear_layout);
        searchLocation=(EditText)findViewById(R.id.et_search_location);
        searchLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                    if(s.length()>2){
                        pbSearch.setVisibility(View.VISIBLE);
                        if(generateNewToken){
                            token = AutocompleteSessionToken.newInstance();
                            generateNewToken = false;
                        }
                        RectangularBounds bounds = RectangularBounds.newInstance(
                                new LatLng(30.0519792, 71.3039295),new LatLng(30.3105332, 71.64538709999999));
                        request = FindAutocompletePredictionsRequest.builder()
                                // Call either setLocationBias() OR setLocationRestriction().
                                .setLocationRestriction(bounds)
                                .setSessionToken(token)
                                .setQuery(s.toString())
                                .build();
                        placesClient.findAutocompletePredictions(request).addOnSuccessListener(new OnSuccessListener<FindAutocompletePredictionsResponse>() {
                            @Override
                            public void onSuccess(FindAutocompletePredictionsResponse response) {
                                locationAdapter.clear();
                                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                    pbSearch.setVisibility(View.INVISIBLE);
                                    String pText=prediction.getPrimaryText(null).toString();
                                    String sText=prediction.getSecondaryText(null).toString();
                                    String placeId=prediction.getPlaceId();
                                    locationAdapter.add(new SavedLocationData(pText,sText,placeId,token));

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                pbSearch.setVisibility(View.INVISIBLE);
                                if (exception instanceof ApiException) {
                                    ApiException apiException = (ApiException) exception;
//                                    Log.e("Places", "Place not found: " + apiException.getStatusCode());
                                }
                            }
                        });

                    }else if(s.length()==0){
                        locationAdapter.clear();
                    }

            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();
//        Log.e("resume","Yo i'm here");

    }


}
