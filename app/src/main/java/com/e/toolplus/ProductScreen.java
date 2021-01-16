package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.adapter.ProductScreenAdapter;

import com.e.toolplus.api.ProductService;

import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Product;

import com.e.toolplus.databinding.ActivityProductScreenBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.PulseRing;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductScreen extends AppCompatActivity {

    ProductScreenAdapter adapter;
    ArrayList<Product> list1;
    ActivityProductScreenBinding binding;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductScreenBinding.inflate(LayoutInflater.from(ProductScreen.this));
        setContentView(binding.getRoot());

        InternetConnection internetConnection = new InternetConnection();
        registerReceiver(internetConnection, InternetIntentFilter.getIntentFilter());

        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra("category");

        initComponent();

        Sprite doubleBounce = new Circle();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);

        ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
        Call<ArrayList<Product>> list = productAPI.getProductByCategory(category.getCategoryId());
        list.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                list1 = response.body();

                adapter = new ProductScreenAdapter(ProductScreen.this, list1);
                binding.rvProductScreen.setAdapter(adapter);
                binding.rvProductScreen.setLayoutManager(new GridLayoutManager(ProductScreen.this, 2));

                binding.spinKit.setVisibility(View.INVISIBLE);

                adapter.setOnItemClick(new ProductScreenAdapter.OnRecyclerViewItemClick() {
                    @Override
                    public void onItemClick(Product product, int position) {
                        Intent intent1 = new Intent(ProductScreen.this, ProductDetailScreen.class);
                        intent1.putExtra("product", product);
                        intent1.putExtra("category", category);
                        startActivity(intent1);
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Toast.makeText(ProductScreen.this, "" + t, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initComponent() {
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle(category.getCategoryName()+" Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}