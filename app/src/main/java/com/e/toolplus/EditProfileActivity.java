package com.e.toolplus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.api.UserService;
import com.e.toolplus.beans.User;
import com.e.toolplus.databinding.ActivityAddUserBinding;
import com.e.toolplus.utility.FileUtils;
import com.e.toolplus.utility.InternetConnection;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    ActivityAddUserBinding binding;
    Uri imageUri;
    String currentUserId, token1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUserBinding.inflate(LayoutInflater.from(EditProfileActivity.this));
        setContentView(binding.getRoot());
        initComponent();
        final SharedPreferences sPref = getSharedPreferences("User", MODE_PRIVATE);

        dataSetOnUI(sPref);

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
                try {

                    if (InternetConnection.isConnected(EditProfileActivity.this)) {
                        String address1 = binding.userAddress.getText().toString();
                        String name1 = binding.userName.getText().toString();
                        String email1 = binding.userEmail.getText().toString();
                        String number1 = binding.userNumber.getText().toString();
                        if (TextUtils.isEmpty(name1)) {
                            binding.userName.setError("Enter Store Name");
                            return;
                        }
                        if (TextUtils.isEmpty(address1)) {
                            binding.userAddress.setError("Enter Address");
                            return;
                        }
                        if (TextUtils.isEmpty(email1)) {
                            binding.userEmail.setError("Enter Email");
                            return;
                        }
                        if (TextUtils.isEmpty(number1)) {
                            binding.userNumber.setError("Enter Number");
                            return;
                        }
                        currentUserId = sPref.getString("userId", "");
                        token1 = sPref.getString("token", "");

                        if (imageUri != null) {
                            final ProgressDialog pd = new ProgressDialog(EditProfileActivity.this,R.style.Theme_MyDialog);
                            pd.setTitle("Updating");
                            pd.setMessage("Please wait");
                            pd.show();
                            File file = FileUtils.getFile(EditProfileActivity.this, imageUri);
                            RequestBody requestFile =
                                    RequestBody.create(
                                            MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                            file
                                    );

                            MultipartBody.Part body =
                                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                            RequestBody name = RequestBody.create(okhttp3.MultipartBody.FORM, name1);
                            RequestBody mobile = RequestBody.create(okhttp3.MultipartBody.FORM, number1);
                            RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, email1);
                            RequestBody address = RequestBody.create(okhttp3.MultipartBody.FORM, address1);
                            RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, token1);
                            RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, currentUserId);

                            UserService.UserAPI api = UserService.getUserAPIInstance();
                            Call<User> call = api.updateUser(body, name, address, mobile, email, token, userId);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.code() == 200) {
                                        User user = response.body();
                                        SharedPreferences.Editor editor = sPref.edit();

                                        editor.putString("userId", user.getUserId());
                                        editor.putString("address", user.getAddress());
                                        editor.putString("email", user.getEmail());
                                        editor.putString("contact", user.getMobile());
                                        editor.putString("token", user.getToken());
                                        editor.putString("imageUrl", user.getImageUrl());
                                        editor.putString("name", user.getName());

                                        editor.commit();
                                        Toast.makeText(EditProfileActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                        pd.dismiss();
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    pd.dismiss();
                                    Log.e("failure","======>"+t);
                                }
                            });

                        } else {
                            final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,R.style.Theme_MyDialog);
                            progressDialog.setTitle("Updating");
                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            User user = new User(currentUserId,name1,number1,address1,sPref.getString("imageUrl",""),email1,token1);
                            UserService.UserAPI api = UserService.getUserAPIInstance();
                            Call<User> call = api.updateUserWithoutImage(user);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.code() == 200) {
                                        progressDialog.dismiss();
                                        User user1 = response.body();
                                        SharedPreferences.Editor editor = sPref.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(user1);
                                        editor.putString(currentUserId, json);
                                        editor.commit();
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.e("Failure without image","=====> "+t);
                                }
                            });
                        }
                    }

                } catch (Exception e) {

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.userImage);
            Toast.makeText(this, "" + imageUri, Toast.LENGTH_SHORT).show();
        }
    }

    public void initComponent() {
        binding.toolbar.setTitle("Profile");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void dataSetOnUI(SharedPreferences sPref) {
        binding.userName.setText(sPref.getString("name", "Name"));
        binding.userNumber.setText(sPref.getString("mobile", "Mobile Number"));
        binding.userAddress.setText(sPref.getString("address", "Address"));
        binding.userEmail.setText(sPref.getString("email", "Email"));
        Picasso.get().load(sPref.getString("imageUrl", "")).placeholder(R.drawable.logo_white).into(binding.userImage);
        binding.tvSaveUser.setText("Update Profile");
    }
}