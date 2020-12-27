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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.api.UserService;
import com.e.toolplus.beans.User;
import com.e.toolplus.databinding.ActivityAddUserBinding;
import com.e.toolplus.utility.FileUtils;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        final SharedPreferences sPref = getSharedPreferences("User", MODE_PRIVATE);

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

                String userAddress = binding.userAddress.getText().toString();
                String userName = binding.userName.getText().toString();
                String userEmail = binding.userEmail.getText().toString();
                String userNumber = binding.userNumber.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    binding.userName.setError("Enter Store Name");
                    return;
                }
                if (TextUtils.isEmpty(userAddress)) {
                    binding.userAddress.setError("Enter Address");
                    return;
                }
                if (TextUtils.isEmpty(userEmail)) {
                    binding.userEmail.setError("Enter Email");
                    return;
                }
                if (TextUtils.isEmpty(userNumber)) {
                    binding.userNumber.setError("Enter Number");
                    return;
                }

                String token2 = FirebaseInstanceId.getInstance().getToken();
                if (imageUri != null) {
                    final ProgressDialog pd = new ProgressDialog(AddUser.this, R.style.Theme_MyDialog);
                    pd.setTitle("Saving");
                    pd.setMessage("Please wait");
                    pd.show();

                    File file = FileUtils.getFile(AddUser.this, imageUri);

                    RequestBody fileRequest = RequestBody.create(
                            MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))), file);

                    Log.e("Line 106 ", "===========> done");
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData("file", file.getName(), fileRequest);

                    RequestBody name = RequestBody.create(okhttp3.MultipartBody.FORM, userName);
                    RequestBody mobile = RequestBody.create(okhttp3.MultipartBody.FORM, userNumber);
                    RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, userEmail);
                    RequestBody address = RequestBody.create(okhttp3.MultipartBody.FORM, userAddress);
                    RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, token2);
                    RequestBody userId = RequestBody.create(okhttp3.MultipartBody.FORM, currentUserId);

                    Log.e("Line 114", "==========>  done");
                    Log.e("Request body ", "====>" + name);

                    UserService.UserAPI api = UserService.getUserAPIInstance();
                    Call<User> call = api.saveUser(body, name, address, mobile, email, token, userId);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                pd.dismiss();
                                User user = response.body();

                                SharedPreferences.Editor editor = sPref.edit();

                                editor.putString("name", user.getName());
                                editor.putString("address", user.getAddress());
                                editor.putString("mobile", user.getMobile());
                                editor.putString("email", user.getEmail());
                                editor.putString("token", user.getToken());
                                editor.putString("imageUrl", user.getImageUrl());
                                editor.putString("userId", user.getUserId());

                                editor.commit();
                                Toast.makeText(AddUser.this, "Saved ", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            if (response.code() == 404) {
                                pd.dismiss();
                                Toast.makeText(AddUser.this, "Something went Wrong...", Toast.LENGTH_SHORT).show();
                            }
                            if (response.code() == 500) {
                                pd.dismiss();
                                Log.e("response code ", "============> " + 505);
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            pd.dismiss();
                            Toast.makeText(AddUser.this, "Failure====>    " + t, Toast.LENGTH_SHORT).show();
                            Log.e("failure", " ==== > " + t.toString());
                        }
                    });
                } else {
                    Toast.makeText(AddUser.this, "Please Select the Profile Image", Toast.LENGTH_SHORT).show();
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
