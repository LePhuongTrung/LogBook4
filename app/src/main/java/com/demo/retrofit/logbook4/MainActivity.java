package com.demo.retrofit.logbook4;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 10142022;
    private static final int CAPTURE_CODE = 1010;
    ImageView img;
    TextInputLayout urlEnter;
    TextInputEditText urlEnterEdit;
    MaterialButton takePicture;

    urlDatabase DB;
    ArrayList<String> link ;
    Uri imgUri;

    int i = 0,check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEnter = findViewById(R.id.textInputLayout);
        img = findViewById(R.id.imageView);
        takePicture = findViewById(R.id.takePicture);

        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
            }
        });


        DB = new  urlDatabase(MainActivity.this);
        link = new ArrayList<>();

        storeDataInArrays();
        check = link.size();
        if (check == 0){
            urlEnterEdit = findViewById(R.id.urlEnter);
            urlEnterEdit.setText("No photos yet, please add a new one");
        } else {
            extracted();}

    }

    //set img
    private void extracted() {
        Glide.with(MainActivity.this)
                .load(link.get(i))
                .centerCrop()
                .into(img);
    }

    public void nextImg(View view){
        i++;
        if (i >=link.size())
            i = 0;
        extracted();
    }

    public void previousImg(View view){
        i--;
        if (i <0)
            i = link.size() -1;
        extracted();
    }
    public void addLink(View view){
        String urlStr = urlEnter.getEditText().getText().toString();

        if (isValidURL(urlStr) == true){
            DB.insertUrl(urlStr);
            storeDataInArrays();
            i = link.size() -1;
            extracted();
        } else {
            Toast.makeText(this, "Url is not vail Please enter again", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidURL(String urlStr) {
        try{
            new URL(urlStr).toURI();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void storeDataInArrays(){
        Cursor cursor = DB.readAllData();
        if(cursor.getCount() == 0){
            urlEnterEdit = findViewById(R.id.urlEnter);
            urlEnterEdit.setText("No photos yet, please add a new one");
        }else{
            while (cursor.moveToNext()){
                link.add(cursor.getString(1));
            }
        }
    }


    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"new image");
        imgUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera();
                } else {
                    Toast.makeText(this, "Permission is blocked", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            img.setImageURI(imgUri);
        }
    }
}