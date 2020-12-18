package com.e.toolplus.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.toolplus.FavoriteProductDetail;
import com.e.toolplus.adapter.FavoriteProductAdapter;
import com.e.toolplus.api.FavoriteService;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.databinding.FragmentFavouriteBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.PulseRing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends Fragment {

    FavoriteProductAdapter adapter;
    FragmentFavouriteBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        binding = FragmentFavouriteBinding.inflate(LayoutInflater.from(getContext()));

        if(!InternetConnection.isConnected(getContext())){
            CustomAlertDialog.internetWarning(getContext());
        }

        if (InternetConnection.isConnected(getContext())){
            Sprite doubleBounce = new PulseRing();
            binding.spinKitFav.setIndeterminateDrawable(doubleBounce);
            FavoriteService.FavoriteAPI api = FavoriteService.getFavoriteAPIInstance();
            Call<ArrayList<Favorite>> list = api.getFavorite(currentUserId);
            list.enqueue(new Callback<ArrayList<Favorite>>() {
                @Override
                public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                    ArrayList<Favorite> list1 = response.body();

                    adapter = new FavoriteProductAdapter(getContext(), list1);
                    binding.rvFavorite.setLayoutManager(new GridLayoutManager(getContext(),2));
                    binding.rvFavorite.setAdapter(adapter);
                    binding.spinKitFav.setVisibility(View.INVISIBLE);

                    adapter.setOnItemClick(new FavoriteProductAdapter.OnRecyclerViewItemClick() {
                        @Override
                        public void onItemClick(Favorite favorite, int position) {
                            Intent in = new Intent(getContext(), FavoriteProductDetail.class);
                            in.putExtra("favorite",favorite);
                            startActivity(in);
                        }
                    });
                }

                @Override
                public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

                }
            });

        }

        return binding.getRoot();
    }
}