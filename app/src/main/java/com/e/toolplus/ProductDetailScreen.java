package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.e.toolplus.databinding.ActivityProductDetailScreenBinding;

public class ProductDetailScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductDetailScreenBinding binding = ActivityProductDetailScreenBinding.inflate(LayoutInflater.from(ProductDetailScreen.this));
        setContentView(binding.getRoot());
    }
}