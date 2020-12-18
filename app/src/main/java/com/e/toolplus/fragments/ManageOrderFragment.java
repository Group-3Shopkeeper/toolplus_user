package com.e.toolplus.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.toolplus.R;
import com.e.toolplus.api.OrderService;
import com.e.toolplus.beans.Order;
import com.e.toolplus.databinding.FragmentManageOrderBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageOrderFragment extends Fragment {
    FragmentManageOrderBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManageOrderBinding.inflate(LayoutInflater.from(getContext()));

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(InternetConnection.isConnected(getContext())){
            OrderService.OrderAPI orderAPI = OrderService.getOrderAPIInstance();
            Call<ArrayList<Order>> listCall = orderAPI.getPlacedOrder(currentUserId);
            listCall.enqueue(new Callback<ArrayList<Order>>() {
                @Override
                public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                    ArrayList<Order> list = response.body();
                    for (Order order : list){
                        Log.e("list data",""+order.getUserId());
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Order>> call, Throwable t) {

                }
            });
        }

        if(!InternetConnection.isConnected(getContext())){
            CustomAlertDialog.internetWarning(getContext());
        }

        return binding.getRoot();
    }
}