package com.e.toolplus.adapter;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplus.R;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.databinding.OrderitemsBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BuyCartAdapter extends RecyclerView.Adapter<BuyCartAdapter.BuyCartViewHolder> {

    ArrayList<Cart> list;
    Context context;
    TextView total;
    long totalAmount;

    public BuyCartAdapter(Context context, ArrayList<Cart> list, TextView total) {
        this.list = list;
        this.total = total;
        this.context = context;
    }

    @NonNull
    @Override
    public BuyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderitemsBinding binding = OrderitemsBinding.inflate(LayoutInflater.from(context), parent, false);
        return new BuyCartViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final BuyCartViewHolder holder, int position) {
        final Cart cart = list.get(position);
        cart.setQty(1);
        Picasso.get().load(cart.getImageUrl()).placeholder(R.drawable.logo_white).into(holder.binding.cartProductImage);
        holder.binding.cartProductName.setText(cart.getName());
        holder.binding.cartProductPrice.setText(" " + cart.getPrice());
        holder.binding.availableStock.setText("Available Stock : "+cart.getQtyInStock());
        //totalAmount = Long.parseLong(total.toString());
        holder.binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(holder.binding.qtyOfStock.getText().toString());
                if (num < cart.getQtyInStock()) {
                    String a = total.getText().toString();
                    totalAmount = Long.parseLong(a);
                    totalAmount = totalAmount+cart.getPrice();
                    num++;
                    holder.binding.qtyOfStock.setText(""+num);
                    cart.setQty(num);
                    total.setText(""+totalAmount);
                    cart.setTotal(num*cart.getPrice());

                } else {
                    Toast.makeText(context, "Out Of Stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.binding.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAmount = totalAmount+cart.getPrice();
                int num = Integer.parseInt(holder.binding.qtyOfStock.getText().toString());
                if (num > 1) {
                    String a = total.getText().toString();
                    totalAmount = Long.parseLong(a);
                    num--;
                    holder.binding.qtyOfStock.setText(""+num);
                    cart.setQty(num);
                    total.setText(""+totalAmount);
                    cart.setTotal(num*cart.getPrice());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BuyCartViewHolder extends RecyclerView.ViewHolder {
        OrderitemsBinding binding;

        public BuyCartViewHolder(OrderitemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
