package com.flightsearch.ui.main.activity;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.flightsearch.R;
import com.flightsearch.databinding.ActivityMainBinding;
import com.flightsearch.utils.base.BaseActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.search) {
                getNavController().navigate(R.id.searchFragment);
            }
            if (item.getItemId() == R.id.my_trips) {
                getNavController().navigate(R.id.myTripsFragment);
            }
            if (item.getItemId() == R.id.profile) {
                getNavController().navigate(R.id.profileFragment);
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        navController = getNavController();
        int currentDestination = navController.getCurrentDestination().getId();
        if (currentDestination == R.id.myTripsFragment || currentDestination == R.id.profileFragment) {
            navController.navigate(R.id.searchFragment);
        } else {
            if (currentDestination == R.id.searchFragment) finish();
            else {
                super.onBackPressed();
            }
        }
    }

    public NavController getNavController() {
        if (navController == null)
            navController = Navigation.findNavController(this, R.id.fragment);
        return navController;
    }
}