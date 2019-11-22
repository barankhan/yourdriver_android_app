package com.baran.driver.Extras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baran.driver.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class LocationDataAdapter extends ArrayAdapter<SavedLocationData> {

    PlacesClient placesClient;
    Activity activity;
    public LocationDataAdapter(Context context, ArrayList<SavedLocationData> locations, PlacesClient p, Activity activity) {

        super(context, 0, locations);
        this.placesClient = p;
        this.activity=activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        SavedLocationData location = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.places_suggestion_layout, parent, false);
        }

        // Lookup view for data population

        TextView tvName = (TextView) convertView.findViewById(R.id.txtsampleinsertion);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        tvName.setTag(R.id.secondary_text,location.getSecondaryText());
        tvName.setTag(R.id.primary_text,location.getPrimaryText());
        tvName.setTag(R.id.place_id,location.getPlaceId());

        // Populate the data into the template view using the data object
        tvName.setText(location.getPrimaryText());


        tvName.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                clickedItem(view);
            }

        });

        // Return the completed view to render on screen

        return convertView;

    }

    private void clickedItem(View v){
        String s =  v.getTag(R.id.place_id).toString();
        Log.e("str",v.getTag(R.id.secondary_text).toString());
        Log.e("str",v.getTag(R.id.primary_text).toString());

        getLocationPlace(s);
    }

    private void getLocationPlace(final String placeId) {
        // Specify the fields to return (in this example all fields are returned).
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        this.placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                Log.e("location_lat", String.valueOf(place.getLatLng().latitude));
                Log.e("location_long", String.valueOf(place.getLatLng().longitude));
                Log.e("location_Name", String.valueOf(place.getName()));


                Intent intent=new Intent();
                intent.putExtra("place",place);
                intent.putExtra("is_saved",0);

                activity.setResult(Activity.RESULT_OK,intent);
                activity.finish();//finishing activity

            }
        });
        this.placesClient.fetchPlace(request).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failed lat long",e.toString());
            }
        });



    }






}
