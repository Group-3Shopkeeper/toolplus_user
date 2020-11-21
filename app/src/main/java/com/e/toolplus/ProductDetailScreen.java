package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.api.CartService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ActivityProductDetailScreenBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
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
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityProductDetailScreenBinding binding = ActivityProductDetailScreenBinding.inflate(LayoutInflater.from(ProductDetailScreen.this));
        setContentView(binding.getRoot());

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

        if (InternetConnection.isConnected(ProductDetailScreen.this)) {
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
                            binding.btnProductDetailCart.setBackgroundColor(getResources().getColor(R.color.addToCart));
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

                }
            });
        }

        binding.btnProductDetailCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InternetConnection.isConnected(ProductDetailScreen.this)) {
                    if (flag == 1) {
                        Toast.makeText(ProductDetailScreen.this, "Product Already Added", Toast.LENGTH_SHORT).show();
                    } else {
                        Cart cart = new Cart();
                        cart.setBrand(product.getBrand());

                        cart.setCategoryId(category.getCategoryId());
                        cart.setUserId(userId);
                        cart.setShopKeeperId(product.getShopKeeperId());
                        cart.setQtyInStock(product.getQtyInStock());
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
                                    binding.btnProductDetailCart.setBackgroundColor(getResources().getColor(R.color.addToCart));
                                    flag = 1;
                                }
                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });

        if(!InternetConnection.isConnected(ProductDetailScreen.this)){
            CustomAlertDialog.internetWarning(ProductDetailScreen.this);
        }
    }
}