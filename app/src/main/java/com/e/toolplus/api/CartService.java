package com.e.toolplus.api;

import com.e.toolplus.beans.Cart;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class CartService {
    public static CartAPI cartAPI;

    public static CartAPI getCartAPIInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(cartAPI == null)
            cartAPI = retrofit.create(CartService.CartAPI.class);
        return cartAPI;
    }

    public interface CartAPI{
        @POST("cart/")
        Call<Cart> saveProductInCart(@Body Cart cart);

        @GET("cart/{currentUserId}")
        Call<ArrayList<Cart>> getCartList(@Path("currentUserId") String currentUserId);

        @DELETE("cart/{cartId}")
        Call<Cart> deleteCartItem(@Path("cartId") String cartId);
    }
}
