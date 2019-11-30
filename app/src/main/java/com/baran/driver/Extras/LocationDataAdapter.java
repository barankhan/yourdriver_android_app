package com.baran.driver.Extras;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baran.driver.Model.DBHelper;
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
import androidx.appcompat.app.AlertDialog;

public class LocationDataAdapter extends ArrayAdapter<SavedLocationData> {

    PlacesClient placesClient;
    Activity activity;
    DBHelper d;
    SavedLocationDataAdapter sa;
    public LocationDataAdapter(Context context, ArrayList<SavedLocationData> locations, PlacesClient p, Activity activity,SavedLocationDataAdapter sa) {

        super(context, 0, locations);
        this.placesClient = p;
        this.activity=activity;
        this.sa = sa;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        final SavedLocationData location = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.places_suggestion_layout, parent, false);
        }

        // Lookup view for data population

        final ImageView im = convertView.findViewById(R.id.img_handle_search_activity);
        im.setImageResource(R.drawable.ic_unsaved_icon);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                im.setTag(R.id.is_saved,false);
                im.setTag(R.id.list_view_position,position);
                im.setTag(R.id.primary_text,location.getPrimaryText());
                im.setTag(R.id.secondary_text,location.getSecondaryText());
                im.setTag(R.id.place_id,location.getPlaceId());
                imageIconClicked(v);
            }
        });

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
                Intent intent=new Intent();
                intent.putExtra("place",place);
                intent.putExtra("is_saved",false);
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



    private void saveLocationPlace(final String placeId, final String primaryText, final String secondaryText, final String title) {
        // Specify the fields to return (in this example all fields are returned).
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        this.placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                d = new DBHelper(getContext());
                Long id = d.insertLocation(placeId,primaryText,secondaryText,String.valueOf(place.getLatLng().latitude),String.valueOf(place.getLatLng().longitude),title);
                SavedLocationData data = d.getData(id);
                sa.insert(data,0);
                sa.notifyDataSetChanged();
                clear();
                notifyDataSetChanged();
            }
        });
        this.placesClient.fetchPlace(request).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Failed lat long",e.toString());
            }
        });



    }

    private void imageIconClicked(final View v) {

        if (!(boolean) v.getTag(R.id.is_saved)) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            final View view = layoutInflater.inflate(R.layout.save_places_dialouge, null);
            final EditText title = view.findViewById(R.id.save_location_title);
            title.setText(v.getTag(R.id.primary_text).toString());
            title.selectAll();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Save Location");


            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            // Write your code here to execute after dialog
                            saveLocationPlace(v.getTag(R.id.place_id).toString(),
                                    v.getTag(R.id.primary_text).toString(),
                                    v.getTag(R.id.secondary_text).toString(),title.getText().toString());

                        }
                    });
            // Setting Negative "NO" Button
            alertDialog.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog
                            dialog.cancel();
                        }
                    });

            // closed

            // Showing Alert Message
            alertDialog.setView(view);
            alertDialog.show();



        }


    }

}
