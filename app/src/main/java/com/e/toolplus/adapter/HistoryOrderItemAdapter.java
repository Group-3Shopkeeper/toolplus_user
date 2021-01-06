package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.R;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.beans.OrderItem;
import com.e.toolplus.databinding.OrderItemHistoryBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryOrderItemAdapter extends RecyclerView.Adapter<HistoryOrderItemAdapter.HistoryOrderItemViewHolder> {
    OnRecyclerCommentClickListener listener;
    ArrayList<OrderItem> list;
    Context context;

    public HistoryOrderItemAdapter(Context context, ArrayList<OrderItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryOrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderItemHistoryBinding binding = OrderItemHistoryBinding.inflate(LayoutInflater.from(context));
        return new HistoryOrderItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryOrderItemViewHolder holder, int position) {
        OrderItem cart = list.get(position);
        holder.binding.tv1.setText(cart.getname());
        holder.binding.tv3.setText("Qty     : " + cart.getQty());
        holder.binding.tv4.setText("Price   : " + cart.getPrice());
        Picasso.get().load(cart.getImageUrl()).placeholder(R.drawable.logo_white).into(holder.binding.iv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HistoryOrderItemViewHolder extends RecyclerView.ViewHolder {
        OrderItemHistoryBinding binding;

        public HistoryOrderItemViewHolder(final OrderItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        OrderItem cart = list.get(position);
                        listener.onItemClick(cart,position);
                    }
                }
            });
        }
    }

    public interface OnRecyclerCommentClickListener {
        void onItemClick(OrderItem orderItem, int position);
    }

    public void setOnClickListener(OnRecyclerCommentClickListener listener) {
        this.listener = listener;
    }
}
