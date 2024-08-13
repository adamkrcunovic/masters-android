package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.databinding.FragmentMyTripsBinding;
import com.flightsearch.ui.main.activity.MainActivity;

public class MyTripsFragment extends Fragment {

    FragmentMyTripsBinding binding;
    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_trips, container, false);
        binding = FragmentMyTripsBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }


}