package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.adapter.OrderHistoryAdapter;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.Order;
import com.e.toolplus.databinding.ActivityHistoryBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding binding;
    OrderHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(LayoutInflater.from(HistoryActivity.this));
        setContentView(binding.getRoot());

        Sprite doubleBounce = new Circle();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initComponent();

        OrderService.OrderAPI api = OrderService.getOrderAPIInstance();
        Call<ArrayList<Order>> orderList = api.getOrders(currentUserId);
        orderList.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                ArrayList<Order> list = response.body();
                if (list.size() != 00) {
                    adapter = new OrderHistoryAdapter(HistoryActivity.this, list);
                    binding.rvHistory.setAdapter(adapter);
                    binding.rvHistory.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    binding.spinKit.setVisibility(View.INVISIBLE);

                    adapter.setOnItemClick(new OrderHistoryAdapter.OnRecyclerItemClick() {
                        @Override
                        public void onItemClick(Order order, int position) {
                            Intent in = new Intent(HistoryActivity.this, BuyCart.class);
                            in.putExtra("reOrder",order);
                            in.putExtra("flag",2);
                            startActivity(in);
                        }
                    });

                } else {
                    binding.rlEmpty.setVisibility(View.VISIBLE);
                    binding.spinKit.setVisibility(View.INVISIBLE);
                    binding.btnBuySomething.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(HistoryActivity.this,HomeActivity.class));
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });

    }
    private void initComponent() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}