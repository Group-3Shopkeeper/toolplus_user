package com.e.toolplus.api;

import com.e.toolplus.beans.BuyCartList;
import com.e.toolplus.beans.Order;
import com.e.toolplus.beans.OrderCartList;
import com.e.toolplus.beans.OrderItem;
import com.e.toolplus.beans.ReOrder;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

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

        @POST("order/cartList")
        Call<OrderCartList> saveOrderByCart(@Body OrderCartList order);

        @GET("order/placed/{currentUserId}")
        Call<ArrayList<Order>> getPlacedOrder(@Path("currentUserId") String currentUserId);

        @POST("order/setQtyOfStock")
        Call<BuyCartList> setQtyOfProduct(@Body BuyCartList list);

        @GET("order/history/{currentUserId}")
        Call<ArrayList<Order>> getOrders(@Path("currentUserId") String currentUserId);

        @POST("order/cancelOrder")
        Call<Order> cancelOrder(@Body Order order);

        @GET("order/reOrder/getQuantity")
        Call<ArrayList<ReOrder>> getQuantityOfOrderItems(@Body Order order);
    }

}
