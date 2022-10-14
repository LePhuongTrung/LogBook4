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

    SQLiteDatabase db;
    public urlDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null,1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Url_COLUMN + " TEXT) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    void insertUrl(String url){
        ContentValues rowValues = new ContentValues();

        rowValues.put(Url_COLUMN, url);

        db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME,null, rowValues);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
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
}

