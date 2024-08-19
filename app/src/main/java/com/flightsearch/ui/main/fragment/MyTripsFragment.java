package com.flightsearch.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentMyTripsBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.adapter.RVAdapterFlightDeal;
import com.flightsearch.ui.main.adapter.RVAdapterFlightDealItinerary;
import com.flightsearch.ui.main.adapter.RVAdapterTripShort;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutFlightSegmentDTO;
import com.flightsearch.utils.models.out.OutTripDTO;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

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
        if (application.getMyTrips() == null || (application.getMyTrips().get(0).isEmpty() && application.getMyTrips().get(1).isEmpty() && application.getMyTrips().get(2).isEmpty())) {
            binding.tabLayout.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);
        } else {
            binding.tabLayout.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.VISIBLE);
            if (application.getMyTrips() != null && !application.getMyTrips().get(1).isEmpty()) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CURRENT"));
                setLayout(application.getMyTrips().get(1), false);
            }
            if (application.getMyTrips() != null && !application.getMyTrips().get(0).isEmpty()) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText("PAST"));
                setLayout(application.getMyTrips().get(0), false);
            }
            if (application.getMyTrips() != null && !application.getMyTrips().get(2).isEmpty()) {
                binding.tabLayout.addTab(binding.tabLayout.newTab().setText("UPCOMING"));
                setLayout(application.getMyTrips().get(2), true);
            }
            binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
            binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    String tabText = tab.getText().toString();
                    if (tabText.equals("CURRENT")) {
                        activity.getNavController().navigate(MyTripsFragmentDirections.actionMyTripsFragmentToTripFragment(application.getMyTrips().get(1).get(0)));
                    }
                    if (tabText.equals("PAST")) {
                        activity.getNavController().navigate(MyTripsFragmentDirections.actionMyTripsFragmentToTripFragment(application.getMyTrips().get(0).get(0)));
                    }
                    if (tabText.equals("UPCOMING")) {
                        activity.getNavController().navigate(MyTripsFragmentDirections.actionMyTripsFragmentToTripFragment(application.getMyTrips().get(2).get(0)));
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setLoggedOrGuestView();
    }

    private void setLayout(List<OutTripDTO> trips, boolean showPrice) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(new RVAdapterTripShort(trips, showPrice, new RVAdapterTripShort.OnFlightDealClickListener() {
            @Override
            public void onClick(OutTripDTO item) {
                activity.getNavController().navigate(MyTripsFragmentDirections.actionMyTripsFragmentToTripFragment(item));
            }
        }));
    }

    private void setLoggedOrGuestView() {
        binding.constraintLayoutGuest.setVisibility(application.isUserLogged() ? View.GONE : View.VISIBLE);
        binding.coordinatorLayoutLogged.setVisibility(application.isUserLogged() ? View.VISIBLE : View.GONE);
        if (application.isUserLogged()) setTabBar();
    }
}