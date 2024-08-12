package com.flightsearch.ui.intro.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.databinding.FragmentIntroLastBinding;

public class IntroLastFragment extends Fragment {

    FragmentIntroLastBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_last, container, false);
        binding = FragmentIntroLastBinding.bind(view);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        binding.materialButtonRegister.setOnClickListener(v -> {

        });

        binding.materialButtonSignIn.setOnClickListener(v -> {

        });
    }
}