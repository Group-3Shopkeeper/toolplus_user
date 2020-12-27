package com.e.toolplus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.adapter.CartProductAdapter;
import com.e.toolplus.api.CartService;
import com.e.toolplus.api.ProductService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ActivityCartProductDetailBinding;
import com.e.toolplus.databinding.CustomAlertDialogBinding;
import com.e.toolplus.fragments.CartFragment;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartProductDetail extends AppCompatActivity {
    CartProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityCartProductDetailBinding binding = ActivityCartProductDetailBinding.inflate(LayoutInflater.from(CartProductDetail.this));
        setContentView(binding.getRoot());

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        setSupportActionBar(binding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        final Cart cart = (Cart) in.getSerializableExtra("cart");
        String productId = cart.getProductId();


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


        binding.btnCartDetailRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CartProductDetail.this);

                CustomAlertDialogBinding customBinding = CustomAlertDialogBinding.inflate(LayoutInflater.from(getApplicationContext()));
                alert.setView(customBinding.getRoot());

                final AlertDialog alertDialog = alert.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                customBinding.negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                customBinding.positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InternetConnection.isConnected(CartProductDetail.this)) {
                            CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
                            Call<Cart> cartCall = cartAPI.deleteCartItem(cart.getCartId());
                            cartCall.enqueue(new Callback<Cart>() {
                                @Override
                                public void onResponse(Call<Cart> call, Response<Cart> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(CartProductDetail.this, "Product Remove Successfully", Toast.LENGTH_SHORT).show();
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Cart> call, Throwable t) {

                                }
                            });
                        }
                        Intent intent = new Intent(CartProductDetail.this, HomeActivity.class);
                        intent.putExtra("cartDetail", 1);
                        startActivity(intent);
                        finish();
                    }
                });

                alertDialog.show();
            }
        });
    }
    //end of on create
}