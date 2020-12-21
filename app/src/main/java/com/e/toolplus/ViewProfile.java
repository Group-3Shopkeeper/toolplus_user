package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.databinding.ActivityViewProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ViewProfile extends AppCompatActivity {
    ActivityViewProfileBinding binding;
    String currentUserId;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewProfileBinding.inflate(LayoutInflater.from(ViewProfile.this));
        setContentView(binding.getRoot());

        initComponent();

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        sPref = getSharedPreferences("User",MODE_PRIVATE);

        binding.tvName.setText(sPref.getString("name","Name"));
        binding.tvAddress.setText(sPref.getString("address","Address"));
        binding.tvEmail.setText(sPref.getString("email","Email"));
        binding.tvNumber.setText(sPref.getString("mobile","Mobile Number"));
        Picasso.get().load(sPref.getString("imageUrl","")).placeholder(R.drawable.logo_white).into(binding.userImage);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfile.this, EditProfileActivity.class));
            }
        });

        binding.rl1.setVisibility(View.INVISIBLE);
        binding.rl2.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.tvName.setText(sPref.getString("name","Name"));
        binding.tvAddress.setText(sPref.getString("address","Address"));
        binding.tvEmail.setText(sPref.getString("email","Email"));
        binding.tvNumber.setText(sPref.getString("mobile","Mobile Number"));
        Picasso.get().load(sPref.getString("imageUrl","")).placeholder(R.drawable.logo_white).into(binding.userImage);

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProfile.this, EditProfileActivity.class));
            }
        });
    }

    private void initComponent() {
        binding.toolbar.setTitle("My Profile");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}