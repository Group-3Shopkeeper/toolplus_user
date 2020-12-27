package com.e.toolplus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.api.CartService;
import com.e.toolplus.api.FavoriteService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ActivityProductDetailScreenBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailScreen extends AppCompatActivity {

    Product product;
    Category category;
    String userId;
    ArrayList<Cart> list;
    ArrayList<Favorite> favList;
    int flag = 0;
    int flag2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityProductDetailScreenBinding binding = ActivityProductDetailScreenBinding.inflate(LayoutInflater.from(ProductDetailScreen.this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Intent in = getIntent();
        product = (Product) in.getSerializableExtra("product");
        category = (Category) in.getSerializableExtra("category");

        binding.productDetailName.setText(product.getName());
        binding.productDetailPrice.setText("Price : " + product.getPrice());
        binding.productDetailDiscount.setText("Discount : " + product.getDiscount());
        binding.productDetailStocks.setText("Stock Availability : " + product.getQtyInStock());
        binding.productDetailCategory.setText("Category : " + category.getCategoryName());
        binding.productDetailDescription.setText(product.getDescription());
        Picasso.get().load(product.getImageUrl()).into(binding.productDetailImage);


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
                        binding.btnProductDetailCart.setBackgroundColor(getResources().getColor(R.color.buy));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

            }
        });


        FavoriteService.FavoriteAPI favoriteAPI = FavoriteService.getFavoriteAPIInstance();
        Call<ArrayList<Favorite>> listCall2 = favoriteAPI.getFavoriteByCategory(userId, category.getCategoryId());
        listCall2.enqueue(new Callback<ArrayList<Favorite>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                favList = response.body();
                String pId = product.getProductId();
                try {
                    for (Favorite favorite : favList) {
                        if (pId.equals(favorite.getProductId())) {
                            flag2 = 1;
                            binding.imageFavoriteHeart.setImageResource(R.drawable.favorite_btn_filled);
                        }
                    }
                } catch (NullPointerException e) {

                }

            }

            @Override
            public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

            }
        });


        binding.imageFavoriteHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag2 == 1) {
                    for (Favorite favorite : favList) {
                        if (product.getProductId().equals(favorite.getProductId())) {
                            FavoriteService.FavoriteAPI favoriteAPI = FavoriteService.getFavoriteAPIInstance();
                            Call<Favorite> fav = favoriteAPI.deleteFavorite(favorite.getFavoriteId());
                            fav.enqueue(new Callback<Favorite>() {
                                @Override
                                public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                    if (response.isSuccessful()) {
                                        binding.imageFavoriteHeart.setImageResource(R.drawable.favourite_btn);
                                        flag2 = 0;
                                    }
                                }

                                @Override
                                public void onFailure(Call<Favorite> call, Throwable t) {

                                }
                            });
                        }

                        if (flag2 == 0)
                            break;
                    }
                }
                if (flag2 == 0) {
                    Favorite favorite = new Favorite();
                    favorite.setBrand(product.getBrand());
                    favorite.setCategoryId(product.getCategoryId());
                    favorite.setDescription(product.getDescription());
                    favorite.setImageUrl(product.getImageUrl());
                    favorite.setName(product.getName());
                    favorite.setPrice(product.getPrice());
                    favorite.setProductId(product.getProductId());
                    favorite.setShopKeeperId(product.getShopKeeperId());
                    favorite.setUserId(userId);

                    FavoriteService.FavoriteAPI api = FavoriteService.getFavoriteAPIInstance();
                    Call<Favorite> favoriteCall = api.saveProductInFavorite(favorite);
                    favoriteCall.enqueue(new Callback<Favorite>() {
                        @Override
                        public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                            if (response.isSuccessful()) {
                                flag2 = 1;
                                binding.imageFavoriteHeart.setImageResource(R.drawable.favorite_btn_filled);

                                FavoriteService.FavoriteAPI api1 = FavoriteService.getFavoriteAPIInstance();
                                Call<ArrayList<Favorite>> listCall = api1.getFavoriteByCategory(userId, category.getCategoryId());
                                listCall.enqueue(new Callback<ArrayList<Favorite>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                                        favList = response.body();
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<Favorite> call, Throwable t) {

                        }
                    });
                }
            }

        });

        binding.btnProductDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag == 1) {
                    Toast.makeText(ProductDetailScreen.this, "Product Already Added", Toast.LENGTH_SHORT).show();
                } else {
                    Cart cart = new Cart();
                    cart.setBrand(product.getBrand());

                    cart.setCategoryId(category.getCategoryId());
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
                                Toast.makeText(ProductDetailScreen.this, "Product Successfully Added In Cart", Toast.LENGTH_SHORT).show();
                                binding.addToC.setText("Added To Cart");
                                binding.btnProductDetailCart.setBackgroundColor(getResources().getColor(R.color.buy));
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