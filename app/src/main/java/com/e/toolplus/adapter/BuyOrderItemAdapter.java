package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.beans.OrderItem;
import com.e.toolplus.databinding.OrderitemsBinding;

import java.util.ArrayList;

public class BuyOrderItemAdapter extends RecyclerView.Adapter<BuyOrderItemAdapter.BuyOrderItemViewHolder> {
    Context context;
    ArrayList<OrderItem> list;

    public BuyOrderItemAdapter(Context context, ArrayList<OrderItem> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public BuyOrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderitemsBinding binding = OrderitemsBinding.inflate(LayoutInflater.from(context));
        return new BuyOrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyOrderItemViewHolder holder, int position) {
        OrderItem orderItem = list.get(position);

        holder.binding.cartProductName.setText(orderItem.getName());
        holder.binding.availableStock.setText("Available Stock : ");

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BuyOrderItemViewHolder extends RecyclerView.ViewHolder {
        OrderitemsBinding binding;

        public BuyOrderItemViewHolder(OrderitemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
