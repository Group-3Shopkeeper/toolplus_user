package com.e.toolplus.utility;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.toolplus.databinding.OfflineBinding;

public class Offline extends AppCompatActivity {
    OfflineBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OfflineBinding.inflate(LayoutInflater.from(Offline.this));
        setContentView(binding.getRoot());
    }
}
