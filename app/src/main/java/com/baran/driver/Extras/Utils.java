package com.baran.driver.Extras;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baran.driver.Model.DBHelper;
import com.baran.driver.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

public class Utils {

    private  static boolean savedDeleteFlag = false;
    private static  ProgressDialog progressDialog;
    public static BitmapDescriptor getBitmapFromVector(@NonNull Context context,
                                                       @DrawableRes int vectorResourceId) {

        Drawable vectorDrawable = ResourcesCompat.getDrawable(
                context.getResources(), vectorResourceId, null);
        if (vectorDrawable == null) {
            Log.e("Utils Class", "Requested vector resource was not found");
            return BitmapDescriptorFactory.defaultMarker();
        }
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static String getAddressUsingLatLong(@NonNull Context context, double lat, double lng){
        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat,lng,1);



            if(!addresses.isEmpty())
                return addresses.get(0).getFeatureName()+","+addresses.get(0).getAddressLine(0);
            else
                return null;
//            Log.e("GeoCode",addresses.get(0).toString());
//            return addresses.get(0).toString();

        } catch (IOException e) {
            Log.e("Error In get geo Coding",e.toString());
        }
        return null;
    }




    public static String getCityBound(@NonNull Context context, String CityName){
        Geocoder geocoder;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
//            List<Address> addresses = geocoder.getFromLocation(lat,lng,1);
            List<Address> addresses = geocoder.getFromLocationName(CityName,1);



            if(!addresses.isEmpty())
                return addresses.get(0).getFeatureName()+","+addresses.get(0).getAddressLine(0);
            else
                return null;
//            Log.e("GeoCode",addresses.get(0).toString());
//            return addresses.get(0).toString();

        } catch (IOException e) {
            Log.e("Error In get geo Coding",e.toString());
        }
        return null;
    }


    private static boolean doOnTrueResult() {
        return savedDeleteFlag = true;

    }

    private static boolean doOnFalseResult() {
        return  savedDeleteFlag = false;
    }




    public static void removeSavedLocationDialogue(final Context c,final View v,final  ImageView iv){

        new AlertDialog.Builder(c)
                .setMessage("Are you sure you want to Delete "+v.getTag(R.id.title))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete from db
                        DBHelper d = new DBHelper(c);
                        d.deleteLocation((Integer)v.getTag(R.id.location_db_id));
                        v.setTag(R.id.is_saved, false);
                        iv.setImageResource(R.drawable.ic_unsaved_icon);
                    }
                })
                .setNegativeButton("No",null)
                .show();


    }


    public static void showLocationSaveDialogue(final Context c, final View v, final LatLng ltln, final ImageView iv, final TextView pt){
        if (!(boolean) v.getTag(R.id.is_saved)) {
            LayoutInflater layoutInflater = LayoutInflater.from(c);
            final View view = layoutInflater.inflate(R.layout.save_places_dialouge, null);
            final EditText title = view.findViewById(R.id.save_location_title);
            title.setText(pt.getText().toString());
            title.selectAll();

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
            alertDialog.setTitle("Save Location");


            alertDialog.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            // Write your code here to execute after dialog
                            DBHelper d = new DBHelper(c);
                            d.insertShortLocation(String.valueOf(ltln.latitude),String.valueOf(ltln.longitude),title.getText().toString());
                            iv.setImageResource(R.drawable.ic_saved_icon);
                            v.setTag(R.id.is_saved,true);

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

            // Showing Alert ChatMessage
            alertDialog.setView(view);
            alertDialog.show();



        }
    }



    public static PointF calculateDerivedPosition(PointF point,
                                                  double range, double bearing)
    {
        double EarthRadius = 6371000; // m

        double latA = Math.toRadians(point.x);
        double lonA = Math.toRadians(point.y);
        double angularDistance = range / EarthRadius;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                                * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        PointF newPoint = new PointF((float) lat, (float) lon);

        return newPoint;

    }



    public static void showAlertBox(Activity a,String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        AlertDialog d =builder.setTitle(message)
                .setPositiveButton(R.string.ok, null).create();
        d.show();
    }




    public static String getRealPathFromURI(Context c , Uri contentURI) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = c.getContentResolver().query(contentURI, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String mediaPath = cursor.getString(columnIndex);

        // Set the Image in ImageView for Previewing the Media

        cursor.close();

        return  mediaPath;
    }


    public static void showProgressBarSpinner(Context context){
        progressDialog = ProgressDialog.show(context, "","Please Wait...", true);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    public static void dismissProgressBarSpinner(){
       try {
           progressDialog.dismiss();
       }catch (NullPointerException e){

       }

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }




}
