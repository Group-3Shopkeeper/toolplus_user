package com.e.toolplus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.beans.Cart;
import com.e.toolplus.databinding.BottomSheetItemBinding;

import java.util.ArrayList;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    Context context;
    ArrayList<Cart> list;

    public OrderSummaryAdapter(Context context, ArrayList<Cart> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BottomSheetItemBinding binding = BottomSheetItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new OrderSummaryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        Cart cart = list.get(position);
        holder.binding.productName.setText(cart.getName());
        holder.binding.productQty.setText(""+cart.getQty());
        holder.binding.productPrice.setText(""+cart.getPrice()+" ₹");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OrderSummaryViewHolder extends RecyclerView.ViewHolder{
        BottomSheetItemBinding binding;

        public OrderSummaryViewHolder(BottomSheetItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
