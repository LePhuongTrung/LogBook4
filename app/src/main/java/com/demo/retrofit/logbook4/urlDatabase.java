package com.demo.retrofit.logbook4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class urlDatabase extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "UrlImg.db";

    private static final String TABLE_NAME = "Url";
    public static final String ID_COLUMN = "id";
    public static final String Url_COLUMN = "url";
    public static final String Location_COLUMN = "Location";

    SQLiteDatabase db;
    public urlDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Location_COLUMN+ " TEXT ,"
                + Url_COLUMN + " TEXT) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    void insertUrl(String url, String location){
        ContentValues rowValues = new ContentValues();

        rowValues.put(Url_COLUMN, url);
        rowValues.put(Location_COLUMN, location);

        db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME,null, rowValues);
        if(result == -1){
            Toast.makeText(context, "Add fail", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    public void delete(Integer row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(row_id)});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
        return;
    }
}

