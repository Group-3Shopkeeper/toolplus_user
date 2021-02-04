package com.e.toolplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.adapter.CartProductAdapter;
import com.e.toolplus.api.CartService;
import com.e.toolplus.api.ProductService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ActivityCartProductDetailBinding;
import com.e.toolplus.databinding.CustomAlertDialogBinding;

import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartProductDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityCartProductDetailBinding binding = ActivityCartProductDetailBinding.inflate(LayoutInflater.from(CartProductDetail.this));
        setContentView(binding.getRoot());

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        final Cart cart = (Cart) in.getSerializableExtra("cart");
        String productId = cart.getProductId();

        binding.addToC.setText("Remove");
        binding.addToC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.reome,0,0,0);


        ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
        Call<Product> productCall = productAPI.getProductById(productId);
        productCall.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {

                Product product = response.body();

                Picasso.get().load(product.getImageUrl()).into(binding.cartDetailImage);
                binding.cartDetailDescription.setText(product.getDescription());
                binding.cartDetailName.setText(product.getName());
                binding.cartDetailStocks.setText("Stocks : " + product.getQtyInStock());

                if (product.getDiscount() < 1){
                    binding.productMRP.setVisibility(View.GONE);
                    binding.productDetailDiscount.setVisibility(View.GONE);
                    binding.cartDetailPrice.setText("Price : ₹ " + product.getPrice());
                } else {
                    Long actualPrice = (product.getDiscount()*product.getPrice())/100;
                    binding.cartDetailPrice.setText("Price : ₹ "+(product.getPrice() - actualPrice));
                    binding.productMRP.setText("MRP : ₹ "+product.getPrice());
                    binding.productMRP.setPaintFlags(binding.productMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.productDetailDiscount.setText("Off : ("+product.getDiscount()+"%)");
                }

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

                                        Intent intent = new Intent(CartProductDetail.this, HomeActivity.class);
                                        intent.putExtra("cartDetail", 1);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Cart> call, Throwable t) {

                                }
                            });
                        }

                    }
                });
                alertDialog.show();
            }
        });
    }
    //end of on create

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}