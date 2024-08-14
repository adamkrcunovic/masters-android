package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.databinding.FragmentFlightSearchResultBinding;
import com.flightsearch.utils.models.out.OutFlightDTO;

public class FlightSearchResultFragment extends Fragment {

    private FragmentFlightSearchResultBinding binding;

    private OutFlightDTO flightSearchResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_search_result, container, false);
        binding = FragmentFlightSearchResultBinding.bind(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFlightSearchResults();
    }

    private void getFlightSearchResults() {
        flightSearchResults = (OutFlightDTO) FlightSearchResultFragmentArgs.fromBundle(getArguments()).getSearchFlightResult();
    }
}