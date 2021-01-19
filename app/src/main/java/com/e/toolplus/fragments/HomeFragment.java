package com.e.toolplus.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.toolplus.ProductDetailScreen;
import com.e.toolplus.ProductScreen;
import com.e.toolplus.adapter.DiscountedProductAdapter;
import com.e.toolplus.adapter.RecentlyAddedProductAdapter;
import com.e.toolplus.api.CategoryService;
import com.e.toolplus.adapter.CategoryAdapter;
import com.e.toolplus.api.ProductService;
import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.FragmentHomeBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.PulseRing;
import com.github.ybq.android.spinkit.style.Wave;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    CategoryAdapter adapter1;
    DiscountedProductAdapter discountedAdapter;
    RecentlyAddedProductAdapter recentlyAdapter;
    ArrayList<Category> categoryArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(LayoutInflater.from(getContext()));

        Sprite doubleBounce = new Circle();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);

        CategoryService.CategoryAPI categoryAPI = CategoryService.getCategoryAPIInstance();
        Call<ArrayList<Category>> categoryList = categoryAPI.getCategoryList();
        categoryList.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                categoryArrayList = response.body();
                adapter1 = new CategoryAdapter(getContext(), categoryArrayList);
                binding.gv1.setAdapter(adapter1);
                binding.gv1.setLayoutManager(new GridLayoutManager(getContext(), 3));


                adapter1.setOnItemClick(new CategoryAdapter.OnRecyclerViewItemClick() {
                    @Override
                    public void onItemClick(Category category, int position) {
                        Intent in = new Intent(getContext(), ProductScreen.class);
                        in.putExtra("category", category);
                        startActivity(in);
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });

        ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
        Call<ArrayList<Product>> listCall = productAPI.getDiscountedProducts();
        listCall.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                Log.e("Response Code","===========>"+response.code());
                ArrayList<Product> discountedProduct = response.body();

                discountedAdapter = new DiscountedProductAdapter(getContext(), discountedProduct);
                binding.discountedProduct.setAdapter(discountedAdapter);
                binding.discountedProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

                discountedAdapter.setOnItemClick(new DiscountedProductAdapter.OnRecyclerItemClick() {
                    @Override
                    public void onItemClick(Product product, int position) {
                        Intent in = new Intent(getContext(), ProductDetailScreen.class);
                        String C = product.getCategoryId();
                        for (Category category : categoryArrayList) {
                            if (C.equals(category.getCategoryId())) {
                                in.putExtra("category", category);
                                break;
                            }
                        }
                        in.putExtra("product", product);
                        startActivity(in);
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Log.e("failure throwable","==========>"+t);
            }
        });

        ProductService.ProductAPI productAPI2 = ProductService.getProductAPIInstance();
        Call<ArrayList<Product>> list = productAPI2.getRecentlyAddedProduct();
        list.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                ArrayList<Product> arrayList = response.body();
                recentlyAdapter = new RecentlyAddedProductAdapter(getContext(), arrayList);
                binding.recentlyAdded.setAdapter(recentlyAdapter);
                binding.recentlyAdded.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                binding.recentlyAdded.setItemViewCacheSize(2);
                binding.spinKit.setVisibility(View.INVISIBLE);
                binding.rlForCategory1.setVisibility(View.VISIBLE);
                binding.rvBelowGv1.setVisibility(View.VISIBLE);
                binding.rvBelowG.setVisibility(View.VISIBLE);

                recentlyAdapter.setOnItemClick(new RecentlyAddedProductAdapter.OnRecyclerItemClick() {
                    @Override
                    public void onItemClick(Product product, int position) {
                        Intent in = new Intent(getContext(), ProductDetailScreen.class);
                        String C = product.getCategoryId();
                        for (Category category : categoryArrayList) {
                            if (C.equals(category.getCategoryId())) {
                                in.putExtra("category", category);
                                break;
                            }
                        }
                        in.putExtra("product", product);
                        startActivity(in);
                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });

        return binding.getRoot();
    }


}