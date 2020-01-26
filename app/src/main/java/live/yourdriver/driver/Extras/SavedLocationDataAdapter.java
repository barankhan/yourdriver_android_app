package live.yourdriver.driver.Extras;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import live.yourdriver.driver.Model.DBHelper;
import live.yourdriver.driver.R;;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;

public class SavedLocationDataAdapter extends ArrayAdapter<SavedLocationData> {

    Activity activity;
    public SavedLocationDataAdapter(Context context, ArrayList<SavedLocationData> locations, Activity activity) {
        super(context, 0, locations);
        this.activity=activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        final SavedLocationData location = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.places_suggestion_layout, parent, false);
        }

        // Lookup view for data population

        ImageView im = convertView.findViewById(R.id.img_handle_search_activity);
        im.setImageResource(R.drawable.ic_saved_icon);
        im.setTag(R.id.is_saved,true);
        im.setTag(R.id.location_db_id,location.getId());
        im.setTag(R.id.title,location.getTitle());
        im.setTag(R.id.list_view_position,position);

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIconClicked(v);
            }
        });



        final TextView tvName = (TextView) convertView.findViewById(R.id.txtsampleinsertion);
        tvName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        // Populate the data into the template view using the data object
        tvName.setText(location.getTitle());
        tvName.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                clickedSavedItem(view,location);
            }

        });

        // Return the completed view to render on screen

        return convertView;

    }



    private void clickedSavedItem(View v,SavedLocationData location){

        Intent intent=new Intent();
        intent.putExtra("location", location);
        intent.putExtra("is_saved",true);
        activity.setResult(Activity.RESULT_OK,intent);
        activity.finish();//finishing activity

    }



    private void imageIconClicked(final View v){
//            Log.e("Image","clicked");

            if((boolean)v.getTag(R.id.is_saved) ){
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to Delete "+v.getTag(R.id.title))
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Delete from db
                                DBHelper d = new DBHelper(getContext());
                                d.deleteLocation((Integer)v.getTag(R.id.location_db_id));
                                remove(getItem((Integer)v.getTag(R.id.list_view_position)));
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

    }
}
