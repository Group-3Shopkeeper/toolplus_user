package com.e.toolplus.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.e.toolplus.R;
import com.e.toolplus.adapter.OrderHistoryAdapter;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.Order;
import com.e.toolplus.databinding.FragmentManageOrderBinding;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrderFragment extends Fragment {
    FragmentManageOrderBinding binding;
    OrderHistoryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageOrderBinding.inflate(LayoutInflater.from(getContext()));

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Sprite doubleBounce = new Circle();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);

        OrderService.OrderAPI orderAPI = OrderService.getOrderAPIInstance();
        Call<ArrayList<Order>> call = orderAPI.getPlacedOrder(currentUserId);
        call.enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                final ArrayList<Order> list = response.body();
                if (list != null) {
                    adapter = new OrderHistoryAdapter(getContext(), list);
                    binding.rvManagerOrder.setAdapter(adapter);
                    binding.rvManagerOrder.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.spinKit.setVisibility(View.INVISIBLE);

                    adapter.setOnItemClick(new OrderHistoryAdapter.OnRecyclerItemClick() {
                        @Override
                        public void onItemClick(final Order order, int position) {

                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                            View alertView = LayoutInflater.from(getContext()).inflate(R.layout.cancle_order_alert,null,false);
                            alertBuilder.setView(alertView);

                            RelativeLayout yes = alertView.findViewById(R.id.btnYES);
                            RelativeLayout no = alertView.findViewById(R.id.btnNO);

                            final AlertDialog alertDialog = alertBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            alertDialog.getWindow().getAttributes().windowAnimations = R.style.Theme_MaterialComponents_Dialog_Alert_Bridge;

                            yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final ProgressDialog pd = new ProgressDialog(getContext(), R.style.Theme_MyDialog);
                                    pd.setTitle("Saving");
                                    pd.setMessage("Please wait");
                                    pd.show();

                                    OrderService.OrderAPI api = OrderService.getOrderAPIInstance();
                                    Call<Order> orderCall = api.cancelOrder(order);
                                    orderCall.enqueue(new Callback<Order>() {
                                        @Override
                                        public void onResponse(Call<Order> call, Response<Order> response) {
                                            Log.e("response code","=========>"+response.code());
                                            if (response.isSuccessful()){
                                                adapter.notifyDataSetChanged();
                                                pd.dismiss();
                                                alertDialog.dismiss();
                                            } else {
                                                pd.dismiss();
                                                alertDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Order> call, Throwable t) {
                                            pd.dismiss();
                                        }
                                    });
                                }
                            });

                            no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();

                        }
                    });
                } else {

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });

        return binding.getRoot();
    }
}