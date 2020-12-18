package com.e.toolplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.e.toolplus.databinding.ActivityHomeBinding;
import com.e.toolplus.fragments.CartFragment;
import com.e.toolplus.fragments.FavouriteFragment;
import com.e.toolplus.fragments.HomeFragment;
import com.e.toolplus.fragments.ManageOrderFragment;
import com.e.toolplus.utility.CustomAlertDialog;
import com.e.toolplus.utility.InternetConnection;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    ChipNavigationBar chipNavigationBar;
    int Buy = 0;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(HomeActivity.this));
        setContentView(binding.getRoot());
        try {
            Intent in = getIntent();
            Buy = (int) in.getIntExtra("Buy", 0);
            if (Buy == 11) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ManageOrderFragment()).commit();
            }
        } catch (NullPointerException e) {
            Log.e("empty Intent", "" + e);
        }

        initComponent();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new HomeFragment()).commit();
        bottomMenu();

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.syncState();
        navigationDrawerMenu();

        if (!InternetConnection.isConnected(HomeActivity.this)) {
            CustomAlertDialog.internetWarning(HomeActivity.this);
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
                    binding.searchBar.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_favourite) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new FavouriteFragment()).commit();
                    drawerLayout.closeDrawers();
                    binding.searchBar.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_history) {

                } else if (itemId == R.id.nav_ManageOrder) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ManageOrderFragment()).commit();
                    drawerLayout.closeDrawers();
                    binding.searchBar.setVisibility(View.GONE);
                } else if (itemId == R.id.nav_viewProfile) {

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
                        binding.searchBar.setVisibility(View.GONE);
                        fragment = new CartFragment();
                        break;
                    case R.id.bottom_favourite:
                        fragment = new FavouriteFragment();
                        binding.searchBar.setVisibility(View.GONE);
                        break;
                    case R.id.bottom_home:
                        fragment = new HomeFragment();
                        binding.searchBar.setVisibility(View.VISIBLE);
                        break;

                    case R.id.bottom_manageOrder:
                        fragment = new ManageOrderFragment();
                        binding.searchBar.setVisibility(View.GONE);
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

    private void sendUserToLoginScreen() {
        Intent in = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(in);
    }
}
