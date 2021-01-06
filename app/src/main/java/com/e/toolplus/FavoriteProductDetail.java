package com.e.toolplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.toolplus.api.CartService;
import com.e.toolplus.api.ProductService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ActivityCartProductDetailBinding;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteProductDetail extends AppCompatActivity {

    ArrayList<Cart> list;
    Product product;
    String userId;
    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityCartProductDetailBinding binding = ActivityCartProductDetailBinding.inflate(LayoutInflater.from(FavoriteProductDetail.this));
        setContentView(binding.getRoot());

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent in = getIntent();
        Favorite favorite = (Favorite) in.getSerializableExtra("favorite");
        String productId = favorite.getProductId();


        ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
        Call<Product> productCall = productAPI.getProductById(productId);
        productCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                product = response.body();

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

        CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
        Call<ArrayList<Cart>> listCall = cartAPI.getCartList(userId);
        listCall.enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                list = response.body();
                String pId = product.getProductId();
                for (Cart cart : list) {
                    if (pId.equals(cart.getProductId())) {
                        flag = 1;
                        binding.addToC.setText("Already Added");
                        binding.btnCartDetailRemove.setBackgroundColor(getResources().getColor(R.color.buy));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

            }
        });

        binding.btnCartDetailRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == 1) {
                    Toast.makeText(FavoriteProductDetail.this, "Product Already Added", Toast.LENGTH_SHORT).show();
                } else {
                    Cart cart = new Cart();
                    cart.setBrand(product.getBrand());

                    cart.setCategoryId(product.getCategoryId());
                    cart.setUserId(userId);
                    cart.setShopKeeperId(product.getShopKeeperId());

                    cart.setDescription(product.getDescription());
                    cart.setImageUrl(product.getImageUrl());
                    cart.setName(product.getName());
                    cart.setPrice(product.getPrice());

                    cart.setProductId(product.getProductId());

                    CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
                    Call<Cart> cartCall = cartAPI.saveProductInCart(cart);
                    cartCall.enqueue(new Callback<Cart>() {
                        @Override
                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(FavoriteProductDetail.this, "Product Successfully Added In Cart", Toast.LENGTH_SHORT).show();
                                binding.addToC.setText("Added To Cart");
                                binding.btnCartDetailRemove.setBackgroundColor(getResources().getColor(R.color.buy));
                                flag = 1;
                            }
                        }

                        @Override
                        public void onFailure(Call<Cart> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }
}
