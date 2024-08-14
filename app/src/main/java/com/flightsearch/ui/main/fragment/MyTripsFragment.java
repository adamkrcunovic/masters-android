package com.flightsearch.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentMyTripsBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyTripsFragment extends BaseFragment implements ApplicationConstants {

    @Inject
    MainApplication application;

    private FragmentMyTripsBinding binding;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_trips, container, false);
        binding = FragmentMyTripsBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTabBar();
        setOnclickListeners();
    }

    private void setOnclickListeners() {
        binding.materialButtonRegister.setOnClickListener(v -> {
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_BACK.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, false));
        });
        binding.materialButtonSignIn.setOnClickListener(v -> {
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_BACK.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, true));
        });
    }

    private void setTabBar() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("MY TRIPS"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FRIENDS TRIPS"));
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setLoggedOrGuestView();
    }

    private void setLoggedOrGuestView() {
        binding.constraintLayoutGuest.setVisibility(application.isUserLogged() ? View.GONE : View.VISIBLE);
        binding.coordinatorLayoutLogged.setVisibility(application.isUserLogged() ? View.VISIBLE : View.GONE);
    }
}