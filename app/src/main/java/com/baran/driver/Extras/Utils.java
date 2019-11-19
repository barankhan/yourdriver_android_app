package com.baran.driver.Extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

public class Utils {
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

}
