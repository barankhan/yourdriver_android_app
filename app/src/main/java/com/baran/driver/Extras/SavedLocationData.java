package com.baran.driver.Extras;

public class SavedLocationData {
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




}
