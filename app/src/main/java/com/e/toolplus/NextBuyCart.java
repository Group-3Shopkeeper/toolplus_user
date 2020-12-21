package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.e.toolplus.beans.Cart;
import com.e.toolplus.databinding.ActivityNextBuyCartBinding;

import java.util.ArrayList;

public class NextBuyCart extends AppCompatActivity {
    ActivityNextBuyCartBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNextBuyCartBinding.inflate(LayoutInflater.from(NextBuyCart.this));
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ArrayList<Cart> cartList = (ArrayList<Cart>) intent.getSerializableExtra("list");
        String grandTotal = (String) intent.getSerializableExtra("grandTotal");

    }
}