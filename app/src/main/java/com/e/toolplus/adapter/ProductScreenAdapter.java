package com.e.toolplus.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.ProductScreen;
import com.e.toolplus.R;
import com.e.toolplus.api.FavoriteService;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.CustomAlertDialogBinding;
import com.e.toolplus.databinding.ProductScreenItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductScreenAdapter extends RecyclerView.Adapter<ProductScreenAdapter.ProductScreenViewHolder> {
    Context context;
    ArrayList<Product> list;
    OnRecyclerViewItemClick listener;
    OnRecyclerViewItemClick favListener;
    int flag = 0;

    public ProductScreenAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductScreenItemBinding binding = ProductScreenItemBinding.inflate(LayoutInflater.from(context));
        return new ProductScreenViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductScreenViewHolder holder, final int position) {
        final String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final Product product = list.get(position);

        String categoryId = product.getCategoryId();

        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvProductPrice.setText("Discount : " + product.getDiscount());
        holder.binding.tvProductPrice.setText("Price : " + product.getPrice());
        Picasso.get().load(product.getImageUrl()).into(holder.binding.ivProductImage);

        FavoriteService.FavoriteAPI favoriteAPI = FavoriteService.getFavoriteAPIInstance();
        Call<ArrayList<Favorite>> listCall = favoriteAPI.getFavoriteByCategory(currentUser, categoryId);
        listCall.enqueue(new Callback<ArrayList<Favorite>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                try {
                    ArrayList<Favorite> favList = response.body();

                    if (favList.size() != 0) {

                        for (Favorite favorite : favList){
                            String proId = favorite.getProductId();
                            String proId2 = product.getProductId();
                            if (proId.equals(proId2)){
                                holder.binding.imageFavoriteHeart.setImageResource(R.drawable.favorite_btn_filled);
                                flag = 1;
                            }
                        }
                    }
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

            }
        });

        holder.binding.btnAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    Toast.makeText(context, "Product Already Added in Favorite", Toast.LENGTH_SHORT).show();
                }
                if (flag == 0) {

                    Favorite favorite = new Favorite();
                    Product product1 = list.get(position);
                    favorite.setBrand(product1.getBrand());
                    favorite.setDescription(product1.getDescription());
                    favorite.setCategoryId(product1.getCategoryId());
                    favorite.setImageUrl(product1.getImageUrl());
                    favorite.setName(product1.getName());
                    favorite.setPrice(product1.getPrice());
                    favorite.setProductId(product1.getProductId());
                    favorite.setShopKeeperId(product1.getShopKeeperId());
                    favorite.setUserId(currentUser);

                    FavoriteService.FavoriteAPI favoriteAPI = FavoriteService.getFavoriteAPIInstance();
                    Call<Favorite> favoriteCall = favoriteAPI.saveProductInFavorite(favorite);
                    favoriteCall.enqueue(new Callback<Favorite>() {
                        @Override
                        public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                            if (response.isSuccessful()) {
                                holder.binding.imageFavoriteHeart.setImageResource(R.drawable.favorite_btn_filled);
                            }
                        }

                        @Override
                        public void onFailure(Call<Favorite> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ProductScreenViewHolder extends RecyclerView.ViewHolder {
        ProductScreenItemBinding binding;

        public ProductScreenViewHolder(ProductScreenItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnAddToFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && favListener != null) {
                        Product product = list.get(position);
                        favListener.onItemClick(product, position);
                    }
                }
            });

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Product product = list.get(position);
                        listener.onItemClick(product, position);
                    }
                }
            });
        }
    }

    public interface OnRecyclerViewItemClick {
        void onItemClick(Product product, int position);
    }

    public void setOnItemClick(OnRecyclerViewItemClick listener) {
        this.listener = listener;
    }

    public void setOnFavouriteClick(OnRecyclerViewItemClick favListener) {
        this.favListener = favListener;
    }
}
