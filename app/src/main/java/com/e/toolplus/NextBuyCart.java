package com.e.toolplus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.e.toolplus.adapter.OrderSummaryAdapter;
import com.e.toolplus.api.CartService;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.api.UserService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Order;
import com.e.toolplus.beans.User;
import com.e.toolplus.databinding.ActivityNextBuyCartBinding;
import com.e.toolplus.databinding.BottomSheetContainerBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NextBuyCart extends AppCompatActivity {
    ActivityNextBuyCartBinding binding;
    ArrayList<Cart> cartList;
    String userName, userAddress, userMobile, userEmail, userId, date;
    int flag1 = 0;
    long grandTotal,timestamp;
    User user;
    AlertDialog alertDialog;
    AlertDialog.Builder builderDialog;

    OrderSummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNextBuyCartBinding.inflate(LayoutInflater.from(NextBuyCart.this));
        setContentView(binding.getRoot());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final Intent intent = getIntent();
        cartList = (ArrayList<Cart>) intent.getSerializableExtra("list");
        grandTotal = intent.getLongExtra("grandTotal", 0);

        binding.grandTotal.setText("Amount : " + grandTotal);

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        date = sd.format(cdate.getTime());
        timestamp = Calendar.getInstance().getTimeInMillis();


        UserService.UserAPI api = UserService.getUserAPIInstance();
        Call<User> call = api.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                userName = user.getName();
                userAddress = user.getAddress();
                userEmail = user.getEmail();
                userMobile = user.getMobile();

                binding.buyerName.setText(userName);
                binding.buyerAddress.setText(userAddress);
                binding.buyerMobile.setText(userMobile);
                binding.buyerEmail.setText(userEmail);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        binding.btnDeliveryOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(NextBuyCart.this, binding.btnDeliveryOption);

                popup.getMenuInflater().inflate(R.menu.delivery_option, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Fast")) {
                            flag1 = 1;
                            grandTotal = grandTotal + 100;
                            binding.tvDeliveryOption.setText(item.getTitle());
                            binding.detailOfDeliveryOption.setVisibility(View.VISIBLE);
                            binding.detailOfDeliveryOption.setText("Delivered within 2 days and Charges 100");
                        }
                        if (item.getTitle().equals("Normal")) {
                            if (flag1 == 1) {
                                grandTotal = grandTotal - 50;
                                binding.tvDeliveryOption.setText(item.getTitle());
                                binding.detailOfDeliveryOption.setVisibility(View.VISIBLE);
                                binding.detailOfDeliveryOption.setText("Delivered within 5 days and charges 50");
                            }
                            if (flag1 == 0) {
                                grandTotal = grandTotal + 50;
                                binding.tvDeliveryOption.setText(item.getTitle());
                                binding.detailOfDeliveryOption.setVisibility(View.VISIBLE);
                                binding.detailOfDeliveryOption.setText("Delivered within 5 days and charges 50");
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        binding.btnPaymentOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(NextBuyCart.this, binding.btnPaymentOption);

                popup.getMenuInflater().inflate(R.menu.payment_option, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Cash On Delivery")) {
                            binding.tvPaymentOption.setText(item.getTitle());
                        }
                        if (item.getTitle().equals("Payment via UPI")) {
                            binding.tvPaymentOption.setText(item.getTitle());
                        }
                        if (item.getTitle().equals("Debit Cart")) {
                            binding.tvPaymentOption.setText(item.getTitle());
                        }
                        return true;
                    }
                });
            }
        });


        binding.changeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builderDialog = new AlertDialog.Builder(NextBuyCart.this);
                View view = LayoutInflater.from(NextBuyCart.this).inflate(R.layout.change_details, null);
                builderDialog.setView(view);

                final EditText etName = view.findViewById(R.id.etName);
                final EditText etAddress = view.findViewById(R.id.etAddress);
                final EditText etMobile = view.findViewById(R.id.etMobile);
                final EditText etEmail = view.findViewById(R.id.etEmail);

                RelativeLayout btnChange = view.findViewById(R.id.btnChange);
                RelativeLayout btnCancel = view.findViewById(R.id.btnCancel);

                etEmail.setText("" + userEmail);
                etAddress.setText("" + userAddress);
                etName.setText("" + userName);
                etMobile.setText("" + userMobile);

                alertDialog = builderDialog.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                alertDialog.getWindow().getAttributes().windowAnimations = R.style.Theme_MaterialComponents_Dialog_Alert;

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

                btnChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userMobile = etEmail.getText().toString();
                        userName = etName.getText().toString();
                        userAddress = etAddress.getText().toString();
                        userMobile = etMobile.getText().toString();

                        binding.buyerEmail.setText("" + userEmail);
                        binding.buyerAddress.setText("" + userAddress);
                        binding.buyerName.setText("" + userName);
                        binding.buyerMobile.setText("" + userMobile);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        binding.orderSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(NextBuyCart.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_container, null);

                adapter = new OrderSummaryAdapter(NextBuyCart.this, cartList);

                RecyclerView rv = bottomSheetView.findViewById(R.id.rvOrderSummary);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Order order = new Order();
                order.setDeliveryOption(binding.tvDeliveryOption.getText().toString());
                order.setTotalAmount(grandTotal);
                order.setShippingStatus("Placed");
                order.setName(userName);
                order.setDeliveryAddress(userAddress);
                order.setContactNumber(userMobile);
                order.setDate(date);
                order.setUserId(userId);
                order.setCartItem(cartList);

                OrderService.OrderAPI api1 = OrderService.getOrderAPIInstance();
                Call<Order> call1 = api1.saveOrder(order);
                call1.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()){

                            for (Cart cart : cartList){
                                String cartId = cart.getCartId();

                                CartService.CartAPI api2 = CartService.getCartAPIInstance();
                                Call<Cart> call2 = api2.deleteCartItem(cartId);
                                call2.enqueue(new Callback<Cart>() {
                                    @Override
                                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                                        if (response.isSuccessful()){
                                            Intent intent1 = new Intent(NextBuyCart.this,HomeActivity.class);
                                            intent1.putExtra("NextBuy",2);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Cart> call, Throwable t) {

                                    }
                                });
                            }

                            Toast.makeText(NextBuyCart.this, "Order Placed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Log.e("failure",""+t);
                    }
                });

            }
        });
    }
}