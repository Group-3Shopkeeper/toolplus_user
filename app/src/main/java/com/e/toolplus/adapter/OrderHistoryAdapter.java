package com.e.toolplus.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.NextBuyCart;
import com.e.toolplus.OrderItemActivity;
import com.e.toolplus.R;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.Order;
import com.e.toolplus.beans.OrderItem;
import com.e.toolplus.beans.Product;
import com.e.toolplus.databinding.HistoryOrderBinding;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryAdapter  extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {
    Context context;
    ArrayList<Order> list;
    OnRecyclerItemClick listener;
    public  OrderHistoryAdapter(Context context, ArrayList<Order> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HistoryOrderBinding binding = HistoryOrderBinding.inflate(LayoutInflater.from(context));
        return new OrderHistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, final int position) {
        final Order order = list.get(position);

        if (order.getShippingStatus().equals("Cancelled") || order.getShippingStatus().equals("Delivered")){
            holder.binding.tvCancleOrder.setText("Re - Order");
            holder.binding.tvCancleOrder.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.reorder,0);
        }

        holder.binding.orderId.setText(order.getOrderId());
        holder.binding.date.setText(order.getDate());
        holder.binding.amount.setText("₹ "+order.getTotalAmount());

        int numberOfItems = order.getOrderItem().size();
        holder.binding.itemsInOrder.setText(""+numberOfItems);

        holder.binding.shippingStatus.setText(order.getShippingStatus());

        holder.binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<OrderItem> carts = order.getOrderItem();
                Intent intent = new Intent(context, OrderItemActivity.class);
                intent.putExtra("orderItems",carts);
                intent.putExtra("shippingStatus",order.getShippingStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {

        HistoryOrderBinding binding;

        public OrderHistoryViewHolder(final HistoryOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        Order order = list.get(position);
                        listener.onItemClick(order,position);
                    }
                }
            });
        }
    }
    public interface OnRecyclerItemClick{
        void onItemClick(Order order, int position);
    }
    public void setOnItemClick(OnRecyclerItemClick listener){
        this.listener = listener;
    }
}
