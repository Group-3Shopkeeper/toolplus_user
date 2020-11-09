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

public class DiscountedProductAdapter extends RecyclerView.Adapter<DiscountedProductAdapter.DiscountedProductViewHolder> {
    Context context;
    ArrayList<Product> arrayList;
    public DiscountedProductAdapter(Context context, ArrayList<Product> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DiscountedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiscountedProductItemBinding binding = DiscountedProductItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new DiscountedProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountedProductViewHolder holder, int position) {
        Product product = arrayList.get(position);
        Picasso.get().load(product.getImageUrl()).into(holder.binding.discountedProductImage);
        holder.binding.discountedProductName.setText(product.getName());
        holder.binding.discountedProductPrice.setText("Price : "+product.getPrice());
        holder.binding.discountedProductActualPrice.setText("Discount : "+product.getDiscount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DiscountedProductViewHolder extends RecyclerView.ViewHolder {
        DiscountedProductItemBinding binding;

        public DiscountedProductViewHolder(DiscountedProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
