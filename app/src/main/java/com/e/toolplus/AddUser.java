package com.e.toolplus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.databinding.ActivityAddUserBinding;
import com.e.toolplus.utility.InternetConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;


public class AddUser extends AppCompatActivity {
    ActivityAddUserBinding binding;
    String currentUserId;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUserBinding.inflate(LayoutInflater.from(AddUser.this));
        setContentView(binding.getRoot());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences sPref = getSharedPreferences("User", MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }

        binding.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(Intent.createChooser(in, "Select image"), 111);
            }
        });

        binding.btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetConnection.isConnected(AddUser.this)) {
                    String address = binding.userAddress.getText().toString();
                    String name = binding.userName.getText().toString();
                    String email = binding.userEmail.getText().toString();
                    String number = binding.userNumber.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        binding.userName.setError("Enter Store Name");
                        return;
                    }
                    if (TextUtils.isEmpty(address)) {
                        binding.userAddress.setError("Enter Address");
                        return;
                    }
                    if (TextUtils.isEmpty(email)) {
                        binding.userEmail.setError("Enter Email");
                        return;
                    }
                    if (TextUtils.isEmpty(number)) {
                        binding.userNumber.setError("Enter Number");
                        return;
                    }
                    final ProgressDialog pd = new ProgressDialog(AddUser.this);
                    pd.setTitle("Saving");
                    pd.setMessage("Please wait");
                    pd.show();

                    String token = FirebaseInstanceId.getInstance().getToken();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 111 && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.userImage);
        }
    }
}