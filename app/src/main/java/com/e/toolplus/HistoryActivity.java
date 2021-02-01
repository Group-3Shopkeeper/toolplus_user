package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.e.toolplus.databinding.ActivityHistoryBinding;

public class HistoryActivity extends AppCompatActivity {
    ActivityHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(LayoutInflater.from(HistoryActivity.this));
        setContentView(binding.getRoot());

        initComponent();

    }
    private void initComponent() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}