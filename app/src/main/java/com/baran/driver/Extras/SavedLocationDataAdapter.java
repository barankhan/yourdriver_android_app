package com.baran.driver.Extras;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baran.driver.R;

import java.util.ArrayList;

public class SavedLocationDataAdapter extends ArrayAdapter<SavedLocationData> {

    public SavedLocationDataAdapter(Context context, ArrayList<SavedLocationData> locations) {

        super(context, 0, locations);

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
        tvName.setTag(R.id.lat,location.getLat());
        tvName.setTag(R.id.lng,location.getLng());

        // Populate the data into the template view using the data object

        tvName.setText(location.getTitle());

        tvName.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                clickedSavedItem(view);
            }

        });

        // Return the completed view to render on screen

        return convertView;

    }



    private void clickedSavedItem(View v){
        String s =  v.getTag(R.id.place_id).toString();
        Log.e("str",v.getTag(R.id.secondary_text).toString());
        Log.e("str",v.getTag(R.id.primary_text).toString());
        Log.e("str",v.getTag(R.id.lat).toString());
        Log.e("str",v.getTag(R.id.lng).toString());
    }
}
