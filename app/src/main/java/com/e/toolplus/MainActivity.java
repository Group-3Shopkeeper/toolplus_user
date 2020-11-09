package com.e.toolplus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.databinding.ActivityMainBinding;
import com.e.toolplus.utility.InternetConnection;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static java.lang.Thread.sleep;

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

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Please connect to the Internet to Proceed Further").setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
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