package com.baran.driver.Extras;

import android.os.Parcel;
import android.os.Parcelable;

public class SavedLocationData implements Parcelable {
    private  int id;
    private  String title,lat,lng,primary_text,secondary_text,place_id;
    public SavedLocationData(int id,String title,String lat,String lng,String primary_text,String secondary_text,String place_id){
        this.id=id;
        this.title=title;
        this.lat=lat;
        this.lng=lng;
        this.primary_text=primary_text;
        this.secondary_text=secondary_text;
        this.place_id=place_id;
    }


    public SavedLocationData(String primary_text,String secondary_text,String place_id){

        this.primary_text=primary_text;
        this.secondary_text=secondary_text;
        this.place_id=place_id;
    }




    public int getId(){
        return  this.id;
    }

    public String getTitle(){
        return this.title;
    }
    public String getLat(){
        return this.lat;
    }
    public String getLng(){
        return this.lng;
    }
    public String getPrimaryText(){
        return this.primary_text;
    }

    public String getSecondaryText(){
        return  this.secondary_text;
    }


    public String getPlaceId(){
        return  this.place_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.lat);
        dest.writeString(this.lng);
        dest.writeString(this.primary_text);
        dest.writeString(this.secondary_text);
        dest.writeString(this.place_id);
    }

    protected SavedLocationData(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.lat = in.readString();
        this.lng = in.readString();
        this.primary_text = in.readString();
        this.secondary_text = in.readString();
        this.place_id = in.readString();
    }

    public static final Creator<SavedLocationData> CREATOR = new Creator<SavedLocationData>() {
        @Override
        public SavedLocationData createFromParcel(Parcel source) {
            return new SavedLocationData(source);
        }

        @Override
        public SavedLocationData[] newArray(int size) {
            return new SavedLocationData[size];
        }
    };
}
