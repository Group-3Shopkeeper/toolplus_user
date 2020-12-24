package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.e.toolplus.adapter.BuyCartAdapter;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.BuyCartList;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.databinding.ActivityBuyCartBinding;

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

        Intent intent = getIntent();
        ArrayList<Cart> list = (ArrayList<Cart>) intent.getSerializableExtra("list");

        BuyCartList list1 = new BuyCartList();
        list1.setList(list);

        OrderService.OrderAPI api = OrderService.getOrderAPIInstance();
        Call<BuyCartList> listCall = api.setQtyOfProduct(list1);
        listCall.enqueue(new Callback<BuyCartList>() {
            @Override
            public void onResponse(Call<BuyCartList> call, Response<BuyCartList> response) {
                BuyCartList cartList = response.body();
                withStock = cartList.getList();
                for (Cart cart : withStock){
                    cart.setQty(1);
                }
                adapter = new BuyCartAdapter(BuyCart.this,withStock);
                binding.rvOrderItem.setAdapter(adapter);
                binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(BuyCart.this));

                calculateGrandTotalPrice();

                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        calculateGrandTotalPrice();
                    }
                });
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
                intent1.putExtra("grandTotal",grandTotal);
                startActivity(intent1);
            }
        });



    }
    public void calculateGrandTotalPrice(){
        grandTotal = 0;
        for (Cart cart : withStock){
            grandTotal = grandTotal + (cart.getQty()*cart.getPrice());
            cart.setTotal(cart.getQty()*cart.getPrice());
        }
        binding.totalAmount.setText("Total Amount : "+grandTotal);
    }

}