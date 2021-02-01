package com.e.toolplus.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.e.toolplus.databinding.CustomAlertDialogBinding;
import com.e.toolplus.databinding.FragmentCartBinding;
import com.e.toolplus.utility.InternetConnection;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {
    String userId;
    FragmentCartBinding binding;
    CartProductAdapter adapter;
    ArrayList<Cart> listOfAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(LayoutInflater.from(getContext()));
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Sprite doubleBounce = new Circle();
        binding.spinKit.setIndeterminateDrawable(doubleBounce);

        CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
        Call<ArrayList<Cart>> listCall = cartAPI.getCartList(userId);
        listCall.enqueue(new Callback<ArrayList<Cart>>() {
            @Override
            public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                listOfAdapter = response.body();
                if (listOfAdapter.size() == 0) {
                    binding.rlBottom.setVisibility(View.INVISIBLE);
                    binding.rlforEmpty.setVisibility(View.VISIBLE);
                }
                adapter = new CartProductAdapter(getContext(), listOfAdapter);
                binding.rvCart.setAdapter(adapter);
                binding.rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.spinKit.setVisibility(View.INVISIBLE);
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
                    public void onItemClick(final Cart cart, final int position) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                        CustomAlertDialogBinding customBinding = CustomAlertDialogBinding.inflate(LayoutInflater.from(getContext()));
                        alert.setView(customBinding.getRoot());

                        final AlertDialog alertDialog = alert.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        customBinding.negative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        customBinding.positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (InternetConnection.isConnected(getContext())) {
                                    String cartId = cart.getCartId();
                                    CartService.CartAPI cartAPI1 = CartService.getCartAPIInstance();
                                    Call<Cart> cartCall = cartAPI1.deleteCartItem(cartId);
                                    cartCall.enqueue(new Callback<Cart>() {
                                        @Override
                                        public void onResponse(Call<Cart> call, Response<Cart> response) {
                                            if (response.isSuccessful()) {
                                                listOfAdapter.remove(position);
                                                adapter.notifyDataSetChanged();
                                                alertDialog.dismiss();
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
                        alertDialog.show();

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

            }
        });


        binding.btnBuyAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartService.CartAPI cartAPI = CartService.getCartAPIInstance();
                Call<ArrayList<Cart>> listCall = cartAPI.getCartList(userId);
                listCall.enqueue(new Callback<ArrayList<Cart>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Cart>> call, Response<ArrayList<Cart>> response) {
                        ArrayList<Cart> list = response.body();
                        if (list.size() == 0) {
                            Toast.makeText(getContext(), "No Product Added", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(getContext(), BuyCart.class);
                            intent.putExtra("list", list);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<ArrayList<Cart>> call, Throwable t) {

                    }
                });

            }
        });

        binding.btnRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}