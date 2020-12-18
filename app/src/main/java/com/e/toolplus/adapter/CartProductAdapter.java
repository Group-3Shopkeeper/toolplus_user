package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.R;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.databinding.CartItemsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {

    ArrayList<Cart> list;
    Context context;
    OnRecyclerViewItemClick listener;
    OnRecyclerViewItemClick listenerRemove;

    public CartProductAdapter(Context context, ArrayList<Cart> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemsBinding binding = CartItemsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CartProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductViewHolder holder, int position) {
        Cart cart = list.get(position);
        holder.binding.cartProductName.setText(cart.getName());
        holder.binding.cartProductPrice.setText("Price : " + cart.getPrice());
        Picasso.get().load(cart.getImageUrl()).placeholder(R.drawable.logo_white).into(holder.binding.cartProductImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartProductViewHolder extends RecyclerView.ViewHolder {
        CartItemsBinding binding;

        public CartProductViewHolder(CartItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.itemRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listenerRemove != null) {
                        Cart cart = list.get(position);
                        listenerRemove.onItemClick(cart, position);
                    }
                }
            });

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        Cart cart = list.get(position);
                        listener.onItemClick(cart, position);
                    }
                }
            });
        }

    }

    public interface OnRecyclerViewItemClick {
        public void onItemClick(Cart cart, int position);
    }

    public void setOnItemClick(OnRecyclerViewItemClick listener) {
        this.listener = listener;
    }

    public void setOnRemoveItemClick(OnRecyclerViewItemClick listenerRemove) {
        this.listenerRemove = listenerRemove;
    }

}
