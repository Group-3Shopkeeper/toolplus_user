package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.adapter.BuyCartAdapter;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Order;
import com.e.toolplus.beans.OrderItem;
import com.e.toolplus.databinding.ActivityBuyCartBinding;
import com.e.toolplus.databinding.OrderitemsBinding;
import com.e.toolplus.fragments.ManageOrderFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyCart extends AppCompatActivity {
    BuyCartAdapter adapter;
    ArrayList<OrderItem> orderItems = new ArrayList<>();
    ActivityBuyCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyCartBinding.inflate(LayoutInflater.from(BuyCart.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ArrayList<Cart> list = (ArrayList<Cart>) intent.getSerializableExtra("list");
        for (Cart cart : list){

            OrderItem orderItem = new OrderItem();

            orderItem.setImageUrl(cart.getImageUrl());
            orderItem.setPrice(cart.getPrice());
            orderItem.setProductName(cart.getName());
            orderItem.setProductId(cart.getProductId());
            orderItem.setShopKeeperId(cart.getShopKeeperId());
            orderItem.setOrderItemId(cart.getUserId());
            orderItem.setQty((long) 1);

            orderItems.add(orderItem);
        }
        adapter = new BuyCartAdapter(BuyCart.this, orderItems);
        binding.rvOrderItem.setAdapter(adapter);
        binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(BuyCart.this));
        calculateTotalPrice();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                calculateTotalPrice();
            }
        });

        binding.btnCartDetailBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.name.getText().toString();
                String address = binding.address.getText().toString();
                String mobile = binding.contactNumber.getText().toString();

                if (TextUtils.isEmpty(name)){
                    binding.name.setError("Enter Name");
                }
                if (TextUtils.isEmpty(address)){
                    binding.name.setError("Enter Current Address");
                }
                if (TextUtils.isEmpty(mobile)){
                    binding.name.setError("Enter Mobile Number");
                }

                Order order = new Order();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                String dateAndTime = sdf.format(new Date());

                String shippingStatus = ("Placed");

                long amount = 0;
                for (OrderItem orderItem : orderItems) {
                    amount = amount + (orderItem.getPrice() * orderItem.getQty());
                }
                order.setUserId(userId);
                order.setDate(dateAndTime);
                order.setContactNumber(mobile);
                order.setDeliveryAddress(address);
                order.setName(name);
                order.setShippingStatus(shippingStatus);

                order.setTotalAmount(amount);
                order.setOrderItem(orderItems);
                order.setDeliveryOption("Fast");

                OrderService.OrderAPI orderAPI = OrderService.getOrderAPIInstance();
                Call<Order> orderCall = orderAPI.saveOrder(order);
                orderCall.enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        if (response.isSuccessful()){
                            Intent in = new Intent(BuyCart.this,HomeActivity.class);
                            in.putExtra("Buy", 11);
                            Toast.makeText(BuyCart.this, "Order Placed Successful", Toast.LENGTH_SHORT).show();
                            startActivity(in);
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {

                    }
                });
            }
        });

    }
    private void calculateTotalPrice(){
        long totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice = totalPrice + (orderItem.getPrice() * orderItem.getQty());
        }
        binding.totalAmount.setText("Total Price : "+totalPrice);
    }
}