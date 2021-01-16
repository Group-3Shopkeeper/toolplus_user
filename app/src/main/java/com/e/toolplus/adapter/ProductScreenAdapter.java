package com.e.toolplus.adapter;


import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.R;
import com.e.toolplus.api.FavoriteService;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.ProductScreenItemBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductScreenAdapter extends RecyclerView.Adapter<ProductScreenAdapter.ProductScreenViewHolder> {
    Context context;
    ArrayList<Product> list;
    OnRecyclerViewItemClick listener;
    OnRecyclerViewItemClick favListener;
    ArrayList<Favorite> favList;
    String categoryId;

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

        categoryId = product.getCategoryId();

        holder.binding.tvProductName.setText(product.getName());

        if (product.getDiscount() != 0) {
            holder.binding.tvProductDiscount.setText("Discount : " + product.getDiscount()+"%");
            long price = product.getPrice();
            long discount = product.getDiscount();
            long discountedPrice = (price*discount)/100;
            holder.binding.tvProductPrice.setText("Price : "+discountedPrice);
        }
        if (product.getDiscount() == 0) {
            holder.binding.tvProductDiscount.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,10,0,0);
            holder.binding.tvProductPrice.setLayoutParams(params);
            holder.binding.tvProductPrice.setText("Price : " + product.getPrice());
        }
        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.logo_white).into(holder.binding.ivProductImage);

        FavoriteService.FavoriteAPI api = FavoriteService.getFavoriteAPIInstance();
        Call<ArrayList<Favorite>> listCall = api.getFavoriteByCategory(currentUser, categoryId);
        listCall.enqueue(new Callback<ArrayList<Favorite>>() {
            @Override
            public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                favList = response.body();
                try {
                    if (favList.size() != 0) {

                        for (Favorite favorite : favList) {
                            String proId = favorite.getProductId();
                            String proId2 = product.getProductId();
                            if (proId.equals(proId2)) {
                                holder.binding.imageFavoriteHeart.setImageResource(R.drawable.favorite_btn_filled);
                                holder.binding.imageFavoriteHeart.setTag("ADDED");
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

                if (holder.binding.imageFavoriteHeart.getTag() == "ADDED") {

                    Product product1 = list.get(position);
                    String favoriteId = null;
                    for (Favorite favorite : favList) {
                        int flag = 1;

                        if (favorite.getProductId().equals(product1.getProductId())) {
                            flag = 0;
                            favoriteId = favorite.getFavoriteId();

                            FavoriteService.FavoriteAPI api1 = FavoriteService.getFavoriteAPIInstance();
                            Call<Favorite> delete = api1.deleteFavorite(favoriteId);
                            delete.enqueue(new Callback<Favorite>() {
                                @Override
                                public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                    if (response.isSuccessful()) {
                                        holder.binding.imageFavoriteHeart.setImageResource(R.drawable.favourite_btn);
                                        holder.binding.imageFavoriteHeart.setTag("DELETE");
                                    }
                                }

                                @Override
                                public void onFailure(Call<Favorite> call, Throwable t) {
                                    Log.e("onFailure",""+t);
                                }
                            });
                        }

                        if(flag == 0)
                            break;
                    }
                }

                if (holder.binding.imageFavoriteHeart.getTag() != "ADDED") {
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
                                holder.binding.imageFavoriteHeart.setTag("ADDED");

                                FavoriteService.FavoriteAPI api = FavoriteService.getFavoriteAPIInstance();
                                Call<ArrayList<Favorite>> listCall = api.getFavoriteByCategory(currentUser, categoryId);
                                listCall.enqueue(new Callback<ArrayList<Favorite>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                                        favList = response.body();
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

                                    }
                                });

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

}
