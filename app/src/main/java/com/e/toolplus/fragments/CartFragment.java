package com.e.toolplus.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.e.toolplus.BuyCart;
import com.e.toolplus.CartProductDetail;
import com.e.toolplus.adapter.CartProductAdapter;
import com.e.toolplus.api.CartService;
import com.e.toolplus.beans.Cart;
import com.e.toolplus.databinding.FragmentCartBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.PulseRing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    String userId;
    FragmentCartBinding binding;
    CartProductAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(LayoutInflater.from(getContext()));
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(InternetConnection.isConnected(getContext())){
            Sprite doubleBounce = new PulseRing();
            binding.spinKitCart.setIndeterminateDrawable(doubleBounce);

            CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
            Call<ArrayList<Cart>> listCall = cartAPI.getCartList(userId);

            listCall.enqueue(new Callback<ArrayList<Cart>>() {
                @Override
                public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                    final ArrayList<Cart> list = response.body();
                    if(list.size() == 0){
                        binding.rlBottom.setVisibility(View.INVISIBLE);
                    }
                    adapter = new CartProductAdapter(getContext(), list);
                    binding.rvCart.setAdapter(adapter);
                    binding.rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.spinKitCart.setVisibility(View.INVISIBLE);
                    adapter.setOnItemClick(new CartProductAdapter.OnRecyclerViewItemClick() {
                        @Override
                        public void onItemClick(Cart cart, int position) {

                            Intent in = new Intent(getContext(), CartProductDetail.class);
                            in.putExtra("cart", cart);
                            startActivity(in);
                        }
                    });

                    adapter.setOnRemoveItemClick(new CartProductAdapter.OnRecyclerViewItemClick() {
                        @Override
                        public void onItemClick(Cart cart, final int position) {
                            if(InternetConnection.isConnected(getContext())){
                                String cartId = cart.getCartId();
                                CartService.CartAPI cartAPI1 = CartService.getCartAPIInstance();
                                Call<Cart> cartCall = cartAPI1.deleteCartItem(cartId);
                                cartCall.enqueue(new Callback<Cart>() {
                                    @Override
                                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                                        if(response.isSuccessful()){
                                            list.remove(position);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(getContext(), "Product Remove Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Cart> call, Throwable t) {

                                    }
                                });
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

                }
            });
        }

        binding.btnBuyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
                Call<ArrayList<Cart>> listCall = cartAPI.getCartList(userId);
                listCall.enqueue(new Callback<ArrayList<Cart>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                        ArrayList<Cart> list = response.body();
                        if(list.size() == 0){
                            Toast.makeText(getContext(), "No Product Added", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent intent = new Intent(getContext(), BuyCart.class);
                            intent.putExtra("list",list);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

                    }
                });

            }
        });

        if(!InternetConnection.isConnected(getContext())){
            CustomAlertDialog.internetWarning(getContext());
        }
        return binding.getRoot();
    }
}