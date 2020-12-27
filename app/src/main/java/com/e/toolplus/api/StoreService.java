package com.e.toolplus.api;

import com.e.toolplus.beans.Store;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class StoreService {
    public static StoreAPI storeAPI;

    public static StoreAPI getStoreAPIInstance() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (storeAPI == null)
            storeAPI = retrofit.create(StoreService.StoreAPI.class);
        return storeAPI;
    }

    public interface StoreAPI {

        @GET("store/{shopkeeperId}")
        Call<Store> getStore(@Path("shopkeeperId") String shopkeeperId);

    }
}
