package com.e.toolplus.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.e.toolplus.R;
import com.e.toolplus.adapter.FavoriteProductAdapter;
import com.e.toolplus.api.FavoriteService;
import com.e.toolplus.beans.Favorite;
import com.e.toolplus.databinding.FragmentFavouriteBinding;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
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

        if(!InternetConnection.isConnected(getContext())){
            CustomAlertDialog.internetWarning(getContext());
        }

        if (InternetConnection.isConnected(getContext())){
            FavoriteService.FavoriteAPI api = FavoriteService.getFavoriteAPIInstance();
            Call<ArrayList<Favorite>> list = api.getFavorite(currentUserId);
            list.enqueue(new Callback<ArrayList<Favorite>>() {
                @Override
                public void onResponse(Call<ArrayList<Favorite>> call, Response<ArrayList<Favorite>> response) {
                    ArrayList<Favorite> list1 = response.body();

                    for(Favorite favorite : list1){
                        Log.e("checking ","=======>>>>"+favorite.getFavoriteId());
                    }

                    adapter = new FavoriteProductAdapter(getContext(), list1);
                    binding.rvFavorite.setAdapter(adapter);
                    binding.rvFavorite.setLayoutManager(new GridLayoutManager(getContext(),2));

                }

                @Override
                public void onFailure(Call<ArrayList<Favorite>> call, Throwable t) {

                }
            });

        }

        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }
}