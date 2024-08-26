package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.databinding.FragmentFlightSearchResultBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.adapter.RVAdapterFlightDeal;
import com.flightsearch.utils.base.bottomSheet.Adapters.RVAdapterRadioButtonText;
import com.flightsearch.utils.models.out.OutFlightDTO;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchResultFragment extends Fragment {

    private FragmentFlightSearchResultBinding binding;
    private MainActivity activity;

    private OutFlightDTO flightSearchResults;
    private int adults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_search_result, container, false);
        binding = FragmentFlightSearchResultBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFlightSearchResults();
        setTabBar();
        setRecyclerView(0);
        setOnBackPressedListener();
    }

    private void getFlightSearchResults() {
        flightSearchResults = (OutFlightDTO) FlightSearchResultFragmentArgs.fromBundle(getArguments()).getSearchFlightResult();
        adults = FlightSearchResultFragmentArgs.fromBundle(getArguments()).getAdults();
    }

    private void setTabBar() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CHEAPEST"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FASTEST"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("CITY VISIT"));
        if (flightSearchResults.getLongestStayFlights() != null && !flightSearchResults.getLongestStayFlights().isEmpty())binding.tabLayout.addTab(binding.tabLayout.newTab().setText("LONGEST STAY"));
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setRecyclerView(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setRecyclerView(int position) {
        List<OutFlightDealDTO> flightsToShowOnScreen = new ArrayList<>();
        if (position == 0) flightsToShowOnScreen = flightSearchResults.getCheapestFlights();
        if (position == 1) flightsToShowOnScreen = flightSearchResults.getFastestFlights();
        if (position == 2) flightsToShowOnScreen = flightSearchResults.getCityVisit();
        if (position == 3) flightsToShowOnScreen = flightSearchResults.getLongestStayFlights();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        RVAdapterFlightDeal adapter = new RVAdapterFlightDeal(flightsToShowOnScreen, new RVAdapterFlightDeal.OnFlightDealClickListener() {
            @Override
            public void onClick(OutFlightDealDTO deal) {
                activity.getNavController().navigate(FlightSearchResultFragmentDirections.actionFlightSearchResultFragmentToTripFragment(deal).setAdults(adults));
            }
        }, position == 2);
        binding.recyclerView.setAdapter(adapter);
    }

    private void setOnBackPressedListener() {
        binding.materialToolbar.setNavigationOnClickListener(v -> {
            activity.onBackPressed();
        });
    }
}