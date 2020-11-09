package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.e.toolplus.databinding.ActivityProductScreenBinding;

public class ProductScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityProductScreenBinding binding = ActivityProductScreenBinding.inflate(LayoutInflater.from(ProductScreen.this));
        setContentView(binding.getRoot());
    }
}