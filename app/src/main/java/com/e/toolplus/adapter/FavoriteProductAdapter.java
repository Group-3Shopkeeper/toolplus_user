package com.e.toolplus.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.R;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.databinding.ProductScreenItemBinding;
import com.e.toolplus.databinding.ProductScreenItemFavoriteBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteProductAdapter extends RecyclerView.Adapter<FavoriteProductAdapter.FavoriteProductViewHolder> {
    ArrayList<Favorite> list;
    Context context;
    OnRecyclerViewItemClick listener;

    public FavoriteProductAdapter(Context context, ArrayList<Favorite> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavoriteProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductScreenItemFavoriteBinding binding = ProductScreenItemFavoriteBinding.inflate(LayoutInflater.from(context));
        return new FavoriteProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteProductViewHolder holder, int position) {
        Favorite favorite = list.get(position);

        holder.binding.tvProductName.setText(favorite.getName());
        holder.binding.tvProductPrice.setText("Price : ₹ "+favorite.getPrice());
        holder.binding.tvProductPrice.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
        Picasso.get().load(favorite.getImageUrl()).placeholder(R.drawable.logo_white).into(holder.binding.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavoriteProductViewHolder extends RecyclerView.ViewHolder {
        ProductScreenItemFavoriteBinding binding;

        public FavoriteProductViewHolder(ProductScreenItemFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Favorite favorite = list.get(position);
                        listener.onItemClick(favorite, position);
                    }
                }
            });
        }
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(Favorite favorite, int position);
    }

    public void setOnItemClick(OnRecyclerViewItemClick listener) {
        this.listener = listener;
    }
}
