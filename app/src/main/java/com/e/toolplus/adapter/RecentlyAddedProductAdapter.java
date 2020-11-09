package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.DiscountedProductItemBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecentlyAddedProductAdapter extends RecyclerView.Adapter<RecentlyAddedProductAdapter.RecentlyAddedProductViewHolder> {
    Context context;
    ArrayList<Product> list;
    public RecentlyAddedProductAdapter(Context context, ArrayList<Product> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecentlyAddedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiscountedProductItemBinding binding = DiscountedProductItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new RecentlyAddedProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlyAddedProductViewHolder holder, int position) {
    Product product = list.get(position);
        Picasso.get().load(product.getImageUrl()).into(holder.binding.discountedProductImage);
        holder.binding.discountedProductName.setText(product.getName());
        holder.binding.discountedProductPrice.setText("Price : "+product.getPrice());
        holder.binding.discountedProductActualPrice.setText("Qty in Stock : "+product.getQtyInStock());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecentlyAddedProductViewHolder extends RecyclerView.ViewHolder{

        DiscountedProductItemBinding binding;

        public RecentlyAddedProductViewHolder(DiscountedProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
