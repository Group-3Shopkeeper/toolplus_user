package com.e.toolplus.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.toolplus.adapter.DiscountedProductAdapter;
import com.e.toolplus.adapter.RecentlyAddedProductAdapter;
import com.e.toolplus.api.CategoryService;
import com.e.toolplus.adapter.CategoryAdapter;
import com.e.toolplus.api.ProductService;
import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.FragmentHomeBinding;
import com.e.toolplus.utility.InternetConnection;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    CategoryAdapter adapter1;
    DiscountedProductAdapter discountedAdapter;
    RecentlyAddedProductAdapter recentlyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(LayoutInflater.from(getContext()));

        if (InternetConnection.isConnected(getContext()))   {
            CategoryService.CategoryAPI categoryAPI = CategoryService.getCategoryAPIInstance();
            Call<ArrayList<Category>> categoryList = categoryAPI.getCategoryList();
            categoryList.enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    ArrayList<Category> categoryArrayList = response.body();

                    adapter1 = new CategoryAdapter(getContext(),categoryArrayList);
                    binding.gv1.setAdapter(adapter1);
                    binding.gv1.setLayoutManager(new GridLayoutManager(getContext(),3));

                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

                }
            });
        }

        if(InternetConnection.isConnected(getContext())){
            ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
            Call<ArrayList<Product>> listCall = productAPI.getDiscountedProducts();
            listCall.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    ArrayList<Product> discountedProduct = response.body();

                    discountedAdapter = new DiscountedProductAdapter(getContext(),discountedProduct);
                    binding.discountedProduct.setAdapter(discountedAdapter);
                    binding.discountedProduct.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                }
            });
        }

        if(InternetConnection.isConnected(getContext())){
            ProductService.ProductAPI productAPI = ProductService.getProductAPIInstance();
            Call<ArrayList<Product>> list = productAPI.getRecentlyAddedProduct();
            list.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    ArrayList<Product> arrayList = response.body();
                    recentlyAdapter = new RecentlyAddedProductAdapter(getContext(),arrayList);
                    binding.recentlyAdded.setAdapter(recentlyAdapter);
                    binding.recentlyAdded.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));

                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                }
            });
        }

        return binding.getRoot();
    }


}