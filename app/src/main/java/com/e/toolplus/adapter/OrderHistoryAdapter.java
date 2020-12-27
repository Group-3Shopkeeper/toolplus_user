package com.e.toolplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.OrderItemActivity;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.Order;
import com.e.toolplus.databinding.OrderHistoryBinding;


import java.util.ArrayList;

public class OrderHistoryAdapter  extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    Context context;
    ArrayList<Order> list;
    public  OrderHistoryAdapter(Context context, ArrayList<Order> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderHistoryBinding binding = OrderHistoryBinding.inflate(LayoutInflater.from(context));
        return new OrderHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        final Order order = list.get(position);
        holder.binding.orderId.setText(order.getOrderId());
        holder.binding.date.setText(order.getDate());
        holder.binding.amount.setText(order.getTotalAmount()+"");

        holder.binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Cart> carts = (ArrayList<Cart>) order.getCartItem();
                Intent intent = new Intent(context, OrderItemActivity.class);
                intent.putExtra("orderItems",carts);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        OrderHistoryBinding binding;

        public OrderHistoryViewHolder(final OrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
