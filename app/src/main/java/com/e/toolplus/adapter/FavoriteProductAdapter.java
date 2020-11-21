package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.api.ProductService;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ProductScreenItemBinding;
import com.e.toolplus.utility.InternetConnection;
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
        ProductScreenItemBinding binding = ProductScreenItemBinding.inflate(LayoutInflater.from(context));
        return new FavoriteProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavoriteProductViewHolder holder, int position) {
        Favorite favorite = list.get(position);
        String productId = favorite.getProductId();

        ProductService.ProductAPI api = ProductService.getProductAPIInstance();
        Call<Product> product = api.getProductById(productId);
        product.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Product product1 = response.body();

                holder.binding.tvProductPrice.setText("Price : " + product1.getPrice());
                holder.binding.tvProductName.setText(product1.getName());
                holder.binding.tvProductDiscount.setText("Discount : " + product1.getDiscount());
                Picasso.get().load(product1.getImageUrl()).into(holder.binding.ivProductImage);

                holder.binding.btnAddToFavourite.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FavoriteProductViewHolder extends RecyclerView.ViewHolder {
        ProductScreenItemBinding binding;

        public FavoriteProductViewHolder(ProductScreenItemBinding binding) {
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
