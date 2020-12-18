package com.e.toolplus.api;

import com.e.toolplus.beans.Order;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class OrderService {
    public static OrderAPI orderAPI;
    public static OrderAPI getOrderAPIInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(orderAPI == null)
            orderAPI = retrofit.create(OrderService.OrderAPI.class);
        return orderAPI;

    }
    public interface OrderAPI{
        @POST("order/")
        Call<Order> saveOrder(@Body Order order);

        @GET("order/placed/{currentUserId}")
        Call<ArrayList<Order>> getPlacedOrder(@Path("currentUserId") String currentUserId);
    }

}
