package com.e.toolplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.toolplus.api.ProductService;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ActivityCartProductDetailBinding;
import com.e.toolplus.utility.InternetConnection;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteProductDetail extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityCartProductDetailBinding binding = ActivityCartProductDetailBinding.inflate(LayoutInflater.from(FavoriteProductDetail.this));
        setContentView(binding.getRoot());

        Intent in = getIntent();
        Favorite favorite = (Favorite) in.getSerializableExtra("favorite");
        String productId = favorite.getProductId();

        if(InternetConnection.isConnected(FavoriteProductDetail.this)){
            ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
            Call<Product> productCall = productAPI.getProductById(productId);
            productCall.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product product = response.body();

                    Picasso.get().load(product.getImageUrl()).into(binding.cartDetailImage);
                    binding.cartDetailDescription.setText(product.getDescription());
                    binding.cartDetailDiscount.setText("Discount : " + product.getDiscount());
                    binding.cartDetailPrice.setText("Price : " + product.getPrice());
                    binding.cartDetailName.setText(product.getName());
                    binding.cartDetailStocks.setText("Stocks : " + product.getQtyInStock());

                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {

                }
            });
        }

    }
}
