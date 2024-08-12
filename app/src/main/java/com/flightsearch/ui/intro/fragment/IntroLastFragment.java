package com.flightsearch.ui.intro.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentIntroLastBinding;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseFragment;

public class IntroLastFragment extends BaseFragment implements ApplicationConstants {

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
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_TO_HOME.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, false));
            getActivity().finish();
        });

        binding.materialButtonSignIn.setOnClickListener(v -> {
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_TO_HOME.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, true));
            getActivity().finish();
        });
    }
}