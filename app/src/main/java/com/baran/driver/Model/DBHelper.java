package com.baran.driver.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.baran.driver.Extras.SavedLocationData;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "_driver.db";
    public static final String LOCATION_TABLE_NAME = "location";
    public static final String RIDE_PATHS_TABLE_NAME = "ride_paths";

    public static final String RIDE_PATHS_COLUMN_ID = "id";
    public static final String RIDE_PATHS_LAT = "lat";
    public static final String RIDE_PATHS_LNG = "lng";
    public static final String RIDE_PATHS_RIDE_ID = "ride_id";

    public static final String LOCATION_COLUMN_ID = "id";
    public static final String LOCATION_COLUMN_GOOGLE_PLACE_ID = "place_id";
    public static final String LOCATION_COLUMN_GOOGLE_PLACE_P_TEXT = "primary_text";
    public static final String LOCATION_COLUMN_GOOGLE_PLACE_S_TEXT = "secondary_text";
    public static final String LOCATION_COLUMN_LAT = "lat";
    public static final String LOCATION_COLUMN_LONG = "lng";
    public static final String LOCATION_COLUMN_TITLE = "title";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table "+LOCATION_TABLE_NAME+" " +
                        "("+LOCATION_COLUMN_ID+" integer primary key, "+LOCATION_COLUMN_GOOGLE_PLACE_ID+" text,"+LOCATION_COLUMN_GOOGLE_PLACE_P_TEXT+" text,"+LOCATION_COLUMN_GOOGLE_PLACE_S_TEXT+" text, "+LOCATION_COLUMN_LAT+" text,"+LOCATION_COLUMN_LONG+" text,"+LOCATION_COLUMN_TITLE +" text )"
        );



        db.execSQL(
                "create table "+RIDE_PATHS_TABLE_NAME+" " +
                        "("+RIDE_PATHS_COLUMN_ID+" integer primary key, "+RIDE_PATHS_LAT+" real,"+RIDE_PATHS_LNG+" real,"+RIDE_PATHS_RIDE_ID+" integer )"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+LOCATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+RIDE_PATHS_TABLE_NAME);
        onCreate(db);
    }

    public long insertLocation (String place_id, String primary_text, String secondary_text, String lat,String lng,String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("place_id", place_id);
        contentValues.put("primary_text", primary_text);
        contentValues.put("secondary_text", secondary_text);
        contentValues.put("lat", lat);
        contentValues.put("lng", lng);
        contentValues.put("title", title);
        return db.insert(LOCATION_TABLE_NAME, null, contentValues);
    }


    public long insertShortLocation (String lat,String lng,String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("lat", lat);
        contentValues.put("lng", lng);
        contentValues.put("title", title);
        return db.insert(LOCATION_TABLE_NAME, null, contentValues);
    }



    public long insertRidePath (Double lat,Double lng,long rideId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RIDE_PATHS_LAT, lat);
        contentValues.put(RIDE_PATHS_LNG, lng);
        contentValues.put(RIDE_PATHS_RIDE_ID, rideId);
        return db.insert(RIDE_PATHS_TABLE_NAME, null, contentValues);
    }



    public SavedLocationData getData(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+LOCATION_TABLE_NAME+" where id="+id+"", null );

        if (res != null) {
            res.moveToFirst();
            int saved_id = res.getInt(res.getColumnIndex(LOCATION_COLUMN_ID));
            String title = res.getString(res.getColumnIndex(LOCATION_COLUMN_TITLE));
            String lat = res.getString(res.getColumnIndex(LOCATION_COLUMN_LAT));
            String lng = res.getString(res.getColumnIndex(LOCATION_COLUMN_LONG));
            String primary_text = res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_P_TEXT));
            String secondary_text = res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_S_TEXT));
            String place_id = res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_ID));
            SavedLocationData s = new SavedLocationData(saved_id, title, lat, lng, primary_text, secondary_text, place_id);
            return s;
        }
        return null;
    }


    public SavedLocationData getDataUsingLatLand(String whereClause) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+LOCATION_TABLE_NAME+" "+whereClause+"", null );
        if (res != null && res.moveToFirst()) {

            int saved_id = res.getInt(res.getColumnIndex(LOCATION_COLUMN_ID));
            String title = res.getString(res.getColumnIndex(LOCATION_COLUMN_TITLE));
            String lat = res.getString(res.getColumnIndex(LOCATION_COLUMN_LAT));
            String lng = res.getString(res.getColumnIndex(LOCATION_COLUMN_LONG));
            String primary_text = res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_P_TEXT));
            String secondary_text = res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_S_TEXT));
            String place_id = res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_ID));
            SavedLocationData s = new SavedLocationData(saved_id, title, lat, lng, primary_text, secondary_text, place_id);
            return s;
        }
        return null;
    }


    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOCATION_TABLE_NAME);
        return numRows;
    }



    public Integer deleteLocation(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(LOCATION_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<SavedLocationData> getAllAddress() {
        List<SavedLocationData> rows = new ArrayList<>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+LOCATION_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            int id = res.getInt(res.getColumnIndex(LOCATION_COLUMN_ID));
            String title = res.getString(res.getColumnIndex(LOCATION_COLUMN_TITLE));
            String lat=res.getString(res.getColumnIndex(LOCATION_COLUMN_LAT));
            String lng=res.getString(res.getColumnIndex(LOCATION_COLUMN_LONG));
            String primary_text =res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_P_TEXT));
            String secondary_text= res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_S_TEXT));
            String place_id = res.getString(res.getColumnIndex(LOCATION_COLUMN_GOOGLE_PLACE_ID));
            SavedLocationData s = new SavedLocationData(id,title,lat,lng,primary_text,secondary_text,place_id);
            rows.add(s);
            res.moveToNext();
        }
        return rows;
    }



    public List<RidePath> getRidePath(int rideId) {
        List<RidePath> rows = new ArrayList<>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+RIDE_PATHS_TABLE_NAME+" WHERE "+RIDE_PATHS_RIDE_ID+" = "+rideId, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            RidePath r = new RidePath();
            r.setId(res.getInt(res.getColumnIndex(RIDE_PATHS_COLUMN_ID)));
            r.setLat(res.getDouble(res.getColumnIndex(RIDE_PATHS_LAT)));
            r.setLng(res.getDouble(res.getColumnIndex(RIDE_PATHS_LNG)));
            r.setRideId(res.getInt(res.getColumnIndex(RIDE_PATHS_RIDE_ID)));
            rows.add(r);
            res.moveToNext();
        }
        return rows;
    }








}