package com.e.toolplus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.adapter.BuyCartAdapter;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.BuyCartList;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Order;
import com.e.toolplus.beans.OrderItem;
import com.e.toolplus.databinding.ActivityBuyCartBinding;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyCart extends AppCompatActivity {
    BuyCartAdapter adapter;
    ActivityBuyCartBinding binding;
    ArrayList<Cart> withStock;
    long grandTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuyCartBinding.inflate(LayoutInflater.from(BuyCart.this));
        setContentView(binding.getRoot());

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        Intent intent = getIntent();
        ArrayList<Cart> list = (ArrayList<Cart>) intent.getSerializableExtra("list");

        Order order = (Order) intent.getSerializableExtra("reOrder");
        int flag = intent.getIntExtra("flag",0);

        BuyCartList list1 = new BuyCartList();
        list1.setList(list);

        OrderService.OrderAPI api = OrderService.getOrderAPIInstance();
        Call<BuyCartList> listCall = api.setQtyOfProduct(list1);
        listCall.enqueue(new Callback<BuyCartList>() {
            @Override
            public void onResponse(Call<BuyCartList> call, Response<BuyCartList> response) {
                Log.e("response of cartlist","========>"+response.code());
                BuyCartList cartList = response.body();
                withStock = cartList.getList();
                for (Cart cart : withStock){
                    cart.setQty(1);
                }
                adapter = new BuyCartAdapter(BuyCart.this,withStock, binding.totalAmount);
                binding.rvOrderItem.setAdapter(adapter);
                binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(BuyCart.this));
                for (Cart cart : withStock){
                    grandTotal = grandTotal+cart.getPrice();
                    binding.totalAmount.setText(""+grandTotal);
                }
            }

            @Override
            public void onFailure(Call<BuyCartList> call, Throwable t) {

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(BuyCart.this,NextBuyCart.class);
                intent1.putExtra("list",withStock);
                Long amount = Long.parseLong(binding.totalAmount.getText().toString());
                intent1.putExtra("grandTotal",amount);
                startActivity(intent1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 5){
            setResult(5);
            finish();
        }
    }
}