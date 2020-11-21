package com.e.toolplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.e.toolplus.adapter.ProductScreenAdapter;

import com.e.toolplus.api.ProductService;

import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Product;

import com.e.toolplus.databinding.ActivityProductScreenBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductScreen extends AppCompatActivity {

    ProductScreenAdapter adapter;
    ArrayList<Product> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityProductScreenBinding binding = ActivityProductScreenBinding.inflate(LayoutInflater.from(ProductScreen.this));
        setContentView(binding.getRoot());

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        Intent intent = getIntent();
        final Category category = (Category) intent.getSerializableExtra("category");

        if (InternetConnection.isConnected(ProductScreen.this)) {
            ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
            Call<ArrayList<Product>> list = productAPI.getProductByCategory(category.getCategoryId());
            list.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    list1 = response.body();

                    adapter = new ProductScreenAdapter(ProductScreen.this, list1);
                    binding.rvProductScreen.setAdapter(adapter);
                    binding.rvProductScreen.setLayoutManager(new GridLayoutManager(ProductScreen.this, 2));


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

        if (!InternetConnection.isConnected(ProductScreen.this)) {

            CustomAlertDialog.internetWarning(ProductScreen.this);

        }
    }
}