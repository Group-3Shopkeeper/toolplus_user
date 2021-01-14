package com.e.toolplus.api;

import com.e.toolplus.beans.Category;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CategoryService {
    public static CategoryAPI categoryAPI;
    public static CategoryAPI getCategoryAPIInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(categoryAPI == null)
            categoryAPI = retrofit.create(CategoryAPI.class);
        return categoryAPI;
    }

    public interface CategoryAPI {

        @GET("category/list")
        public Call<ArrayList<Category>> getCategoryList();

        @GET("category/{categoryId}")
        Call<Category> getCategoryById(@Path("categoryId") String categoryId);
    }
}
