package com.e.toolplus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.adapter.ProductScreenAdapter;
import com.e.toolplus.api.CategoryService;
import com.e.toolplus.api.ProductService;
import com.e.toolplus.api.UserService;
import com.e.toolplus.beans.Category;
import com.e.toolplus.beans.Product;
import com.e.toolplus.beans.User;
import com.e.toolplus.databinding.ActivityHomeBinding;
import com.e.toolplus.fragments.CartFragment;
import com.e.toolplus.fragments.FavouriteFragment;
import com.e.toolplus.fragments.HomeFragment;
import com.e.toolplus.fragments.ManageOrderFragment;
import com.e.toolplus.utility.InternetConnection;
import com.e.toolplus.utility.InternetIntentFilter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ChipNavigationBar chipNavigationBar;
    String name = "";
    ProductScreenAdapter adapter;
    Category category;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(HomeActivity.this));
        setContentView(binding.getRoot());

        InternetConnection internetConnection = new InternetConnection();
        this.registerReceiver(internetConnection,InternetIntentFilter.getIntentFilter());

        initComponent();
        bottomMenu();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();
        binding.bottomNavigation.setItemSelected(R.id.bottom_home,true);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.syncState();

        navigationDrawerMenu();

        binding.ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchBar.setVisibility(View.VISIBLE);

                binding.searchBar.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        Fragment fragment = (Fragment) getSupportFragmentManager().getFragments();
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                        binding.rvForSearch.setVisibility(View.VISIBLE);
                        name = s.toString();
                        getProductName(name);

                        if (name.isEmpty()){
                            binding.searchBar.setVisibility(View.INVISIBLE);
                            binding.rvForSearch.setVisibility(View.INVISIBLE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                binding.searchBar.setVisibility(View.INVISIBLE);
                binding.rvForSearch.setVisibility(View.INVISIBLE);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new HomeFragment()).commit();
            }
        });
    }

    private void getProductName(String name) {
        ProductService.ProductAPI ap = ProductService.getProductAPIInstance();
        Call<ArrayList<Product>> listCall = ap.getProductByName(name);
        listCall.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                ArrayList<Product> list = response.body();
                adapter = new ProductScreenAdapter(getApplicationContext(), list);
                binding.rvForSearch.setAdapter(adapter);
                binding.rvForSearch.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));

                adapter.setOnItemClick(new ProductScreenAdapter.OnRecyclerViewItemClick() {
                    @Override
                    public void onItemClick(Product product, int position) {
                        String categoryId = product.getCategoryId();

                        CategoryService.CategoryAPI api = CategoryService.getCategoryAPIInstance();
                        Call<Category> categoryCall = api.getCategoryById(categoryId);
                        categoryCall.enqueue(new Callback<Category>() {
                            @Override
                            public void onResponse(Call<Category> call, Response<Category> response) {
                                category = response.body();
                            }

                            @Override
                            public void onFailure(Call<Category> call, Throwable t) {

                            }
                        });

                        Intent intent1 = new Intent(getApplicationContext(), ProductDetailScreen.class);
                        intent1.putExtra("product", product);
                        intent1.putExtra("category", category);
                        startActivity(intent1);

                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkUserProfile();

        Intent intent = getIntent();
        int cartDetail = intent.getIntExtra("cartDetail",0);

        if (cartDetail == 1){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new CartFragment()).commit();
            binding.bottomNavigation.setItemSelected(R.id.bottom_cart,true);
        }

        int NextBuy = intent.getIntExtra("NextBuyCart",0);
        if (NextBuy == 420){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new ManageOrderFragment()).commit();
            binding.bottomNavigation.setItemSelected(R.id.bottom_manageOrder,true);
        }

        int single = intent.getIntExtra("SingleProductBuy",0);
        if (single == 120){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,new ManageOrderFragment()).commit();
            binding.bottomNavigation.setItemSelected(R.id.bottom_manageOrder,true);
        }

    }

    private void navigationDrawerMenu() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_Cart) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new CartFragment()).commit();
                    drawerLayout.closeDrawers();
                    binding.bottomNavigation.setItemSelected(R.id.bottom_cart,true);
                    binding.searchBar.setVisibility(View.GONE);

                } else if (itemId == R.id.nav_favourite) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FavouriteFragment()).commit();
                    drawerLayout.closeDrawers();
                    binding.bottomNavigation.setItemSelected(R.id.bottom_favourite,true);
                    binding.searchBar.setVisibility(View.GONE);

                } else if (itemId == R.id.nav_history) {
                    startActivity(new Intent(HomeActivity.this,HistoryActivity.class));

                } else if (itemId == R.id.nav_ManageOrder) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ManageOrderFragment()).commit();
                    drawerLayout.closeDrawers();
                    binding.bottomNavigation.setItemSelected(R.id.bottom_manageOrder,true);
                    binding.searchBar.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_viewProfile) {
                    startActivity(new Intent(getApplicationContext(),ViewProfile.class));
                } else if (itemId == R.id.nav_logOut) {
                    AuthUI.getInstance()
                            .signOut(HomeActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                sendUserToLoginScreen();
                            } else {
                                Toast.makeText(HomeActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                return false;
            }
        });
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.bottom_favourite:
                        fragment = new FavouriteFragment();
                        break;
                    case R.id.bottom_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.bottom_manageOrder:
                        fragment = new ManageOrderFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initComponent() {
        toolbar = binding.toolbar;
        drawerLayout = binding.drawerLayout;
        navigationView = binding.navigationView;
        chipNavigationBar = binding.bottomNavigation;
        setSupportActionBar(toolbar);
    }

    private void sendUserToAddUserActivity() {
        startActivity(new Intent(HomeActivity.this, AddUser.class));
    }

    private void sendUserToLoginScreen() {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    private void checkUserProfile() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        String id = sp.getString("userId", "Not found");

        if (!id.equals("Not found")) {
            if (!id.equals(currentUserId)) {
                UserService.UserAPI api = UserService.getUserAPIInstance();
                Call<User> call = api.getUserById(currentUserId);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200) {
                            User user = response.body();
                            SharedPreferences.Editor editor = sp.edit();

                            editor.putString("name", user.getName());
                            editor.putString("address", user.getAddress());
                            editor.putString("mobile", user.getMobile());
                            editor.putString("email", user.getEmail());
                            editor.putString("token", user.getToken());
                            editor.putString("imageUrl", user.getImageUrl());
                            editor.putString("userId", user.getUserId());
                            editor.commit();
                        } else if (response.code() == 404) {
                            sendUserToAddUserActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        } else {
            UserService.UserAPI api = UserService.getUserAPIInstance();
            Call<User> call = api.getUserById(currentUserId);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.code() == 200) {
                        User user = response.body();
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("name", user.getName());
                        editor.putString("address", user.getAddress());
                        editor.putString("mobile", user.getMobile());
                        editor.putString("email", user.getEmail());
                        editor.putString("token", user.getToken());
                        editor.putString("imageUrl", user.getImageUrl());
                        editor.putString("userId", user.getUserId());
                        editor.commit();
                    }
                    if (response.code() == 404) {
                        sendUserToAddUserActivity();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }

    }

}
