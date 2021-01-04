package com.e.toolplus.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.ProductDetailScreen;
import com.e.toolplus.R;
import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.CategoryWithProductList;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.CategorywithproductitemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryWithProductAdapter extends RecyclerView.Adapter<CategoryWithProductAdapter.CategoryWithProductViewHolder> {
    RecentlyAddedProductAdapter adapter;
    Context context;
    ArrayList<CategoryWithProductList> list;
    public CategoryWithProductAdapter(Context context, ArrayList<CategoryWithProductList> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryWithProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategorywithproductitemBinding binding = CategorywithproductitemBinding.inflate(LayoutInflater.from(context));
        return new CategoryWithProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryWithProductViewHolder holder, int position) {
        final CategoryWithProductList category = list.get(position);
        holder.binding.CategoryName.setText(category.getCategoryName());
        Picasso.get().load(category.getImageUrl()).placeholder(R.drawable.logo_white).into(holder.binding.categoryImage);

        adapter = new RecentlyAddedProductAdapter(context, category.getList());
        holder.binding.allProductOfCategory.setAdapter(adapter);
        holder.binding.allProductOfCategory.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

        adapter.setOnItemClick(new RecentlyAddedProductAdapter.OnRecyclerItemClick() {
            @Override
            public void onItemClick(Product product, int position) {
                Intent intent = new Intent(context, ProductDetailScreen.class);

                Category productsCategory = new Category();

                productsCategory.setCategoryId(category.getCategoryId());
                productsCategory.setCategoryName(category.getCategoryName());
                productsCategory.setImageUrl(category.getImageUrl());

                intent.putExtra("product",product);
                intent.putExtra("category",productsCategory);
                
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryWithProductViewHolder extends RecyclerView.ViewHolder {
        CategorywithproductitemBinding binding;

        public CategoryWithProductViewHolder(CategorywithproductitemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
