package com.e.toolplus;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
    ActivityCartProductDetailBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartProductDetailBinding.inflate(LayoutInflater.from(FavoriteProductDetail.this));
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                if(product!=null) {
                    Picasso.get().load(product.getImageUrl()).into(binding.cartDetailImage);
                    binding.cartDetailDescription.setText(product.getDescription());
                    binding.cartDetailName.setText(product.getName());
                    binding.cartDetailStocks.setText("Stocks : " + product.getQtyInStock());

                    if (product.getDiscount() < 1) {
                        binding.productMRP.setVisibility(View.GONE);
                        binding.productDetailDiscount.setVisibility(View.GONE);
                        binding.cartDetailPrice.setText("Price : ₹ " + product.getPrice());
                    } else {
                        Long actualPrice = (product.getDiscount() * product.getPrice()) / 100;
                        binding.cartDetailPrice.setText("Price : ₹ " + (product.getPrice() - actualPrice));
                        binding.productMRP.setText("MRP : ₹ " + product.getPrice());
                        binding.productMRP.setPaintFlags(binding.productMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        binding.productDetailDiscount.setText("Off : (" + product.getDiscount() + "%)");
                    }
                   checkProductInCart(product);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

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
                                binding.addToC.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                                binding.addToC.setTextColor(Color.WHITE);

                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    binding.btnCartDetailRemove.setBackgroundDrawable(ContextCompat.getDrawable(FavoriteProductDetail.this, R.drawable.already_added) );
                                } else {
                                    binding.btnCartDetailRemove.setBackground(ContextCompat.getDrawable(FavoriteProductDetail.this, R.drawable.already_added));
                                }
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
    private void checkProductInCart(final Product product){
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
                        binding.addToC.setCompoundDrawableTintList(ColorStateList.valueOf(Color.WHITE));
                        binding.addToC.setTextColor(Color.WHITE);

                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            binding.btnCartDetailRemove.setBackgroundDrawable(ContextCompat.getDrawable(FavoriteProductDetail.this, R.drawable.already_added) );
                        } else {
                            binding.btnCartDetailRemove.setBackground(ContextCompat.getDrawable(FavoriteProductDetail.this, R.drawable.already_added));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

            }
        });

        binding.btnCartDetailBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteProductDetail.this,SingleProductBuy.class);
                intent.putExtra("product",product);
                startActivity(intent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
