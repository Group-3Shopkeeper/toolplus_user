package com.e.toolplus.api;

import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.CategoryWithProductList;
import com.e.toolplus.beans.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ProductService {

    public static ProductAPI productAPI;
    public static ProductAPI getProductAPIInstance(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ServerAddress.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(productAPI == null)
            productAPI = retrofit.create(ProductService.ProductAPI.class);
        return productAPI;

    }

    public interface ProductAPI{
        @GET("product/discount")
        public Call<ArrayList<Product>> getDiscountedProducts();

        @GET("product/recent-product")
        public Call<ArrayList<Product>> getRecentlyAddedProduct();

        @GET("product/c/{categoryId}")
        public  Call<ArrayList<Product>> getProductByCategory(@Path("categoryId") String categoryId);

        @GET("product/{productId}")
        public Call<Product> getProductById(@Path("productId") String productId);

        @GET("product/categoryWithProductList")
        Call<ArrayList<CategoryWithProductList>> getCategoryWithProductList(@Body ArrayList<Category> list);
    }
}
