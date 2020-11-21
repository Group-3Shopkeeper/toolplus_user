package com.e.toolplus.api;

import com.e.toolplus.beans.Favorite;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class FavoriteService {
    public static FavoriteAPI favoriteAPI;

    public static FavoriteAPI getFavoriteAPIInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(favoriteAPI == null)
            favoriteAPI = retrofit.create(FavoriteService.FavoriteAPI.class);
        return favoriteAPI;
    }

    public interface FavoriteAPI{
        @POST("favorite/")
        Call<Favorite> saveProductInFavorite(@Body Favorite favorite);

        @GET("favorite/{currentUserId}")
        Call<ArrayList<Favorite>> getFavorite(@Path("currentUserId") String currentUserId);

        @GET("favorite/{currentUserId}/{categoryId}")
        Call<ArrayList<Favorite>> getFavoriteByCategory(@Path("currentUserId") String currentUserId, @Path("categoryId") String categoryId);
    }
}
