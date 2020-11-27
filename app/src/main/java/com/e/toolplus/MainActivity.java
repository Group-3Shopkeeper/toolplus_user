package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.databinding.ActivityMainBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseUser mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        binding = ActivityMainBinding.inflate(inflater);
        View v = binding.getRoot();
        setContentView(v);

        if (!InternetConnection.isConnected(MainActivity.this)) {

            CustomAlertDialog.internetWarning(MainActivity.this);

        }

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (InternetConnection.isConnected(MainActivity.this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mAuth != null) {
                        sendUserToHomeScreen();
                    } else {
                        sendUserToLoginScreen();
                    }
                }
            }, 5000);
        }
    }

    private void sendUserToHomeScreen() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void sendUserToLoginScreen() {
        Intent in = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(in);
        this.finish();
    }
}