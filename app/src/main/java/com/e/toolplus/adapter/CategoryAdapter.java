package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.beans.Category;
import com.e.toolplus.databinding.CategoryHomeItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    Context context;
    ArrayList<Category> list;
    public CategoryAdapter(Context context, ArrayList<Category> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryHomeItemBinding binding = CategoryHomeItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = list.get(position);
        Picasso.get().load(category.getImageUrl()).into(holder.binding.ivCategory);
        holder.binding.tvCategoryName.setText(category.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        CategoryHomeItemBinding binding;

        public CategoryViewHolder(CategoryHomeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
