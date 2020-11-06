package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CreateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        getSupportActionBar().setTitle("Create Profile");
    }

    public void btnCreateProfile(View view) {
       startActivity(new Intent(getApplicationContext(),CreateProfile.class));
    }
}