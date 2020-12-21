package com.e.toolplus.api;

import com.e.toolplus.beans.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class UserService {

    public static UserAPI userAPI;

    public static UserAPI getUserAPIInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (userAPI == null)
            return retrofit.create(UserService.UserAPI.class);
        return userAPI;
    }
    public interface UserAPI{
        @Multipart
        @POST("user/save")
        Call<User> saveUser(@Part MultipartBody.Part file,
                            @Part("name") RequestBody name,
                            @Part("address") RequestBody address,
                            @Part("mobile") RequestBody mobile,
                            @Part("email") RequestBody email,
                            @Part("token") RequestBody token,
                            @Part("userId") RequestBody userId);
        @Multipart
        @POST("user/update")
        Call<User> updateUser(@Part MultipartBody.Part file,
                              @Part("name") RequestBody name,
                              @Part("address") RequestBody address,
                              @Part("mobile") RequestBody mobile,
                              @Part("email") RequestBody email,
                              @Part("token") RequestBody token,
                              @Part("userId") RequestBody userId);

        @POST("user/updateWithoutImage")
        Call<User> updateUserWithoutImage(@Body User user);

        @GET("user/{userId}")
        Call<User> getUserById(@Path("userId")String userId);
    }
}
