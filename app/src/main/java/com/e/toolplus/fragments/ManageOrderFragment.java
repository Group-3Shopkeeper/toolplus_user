package com.e.toolplus.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.toolplus.R;
import com.e.toolplus.adapter.OrderHistoryAdapter;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.Order;
import com.e.toolplus.beans.OrderCartList;
import com.e.toolplus.databinding.FragmentManageOrderBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
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
                ArrayList<Order> list = response.body();
                adapter = new OrderHistoryAdapter(getContext(),list);
                binding.rvManagerOrder.setAdapter(adapter);
                binding.rvManagerOrder.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.spinKit.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

            }
        });

        return binding.getRoot();
    }
}