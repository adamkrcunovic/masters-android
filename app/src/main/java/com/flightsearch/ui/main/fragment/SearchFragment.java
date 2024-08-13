package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flightsearch.R;
import com.flightsearch.databinding.BottomSheetInfoBinding;
import com.flightsearch.databinding.FragmentSearchBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.utils.base.bottomSheet.BottomSheetInfo;
import com.google.android.material.button.MaterialButton;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        binding = FragmentSearchBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        binding.materialButtonExactDatesInActive.setOnClickListener(v -> {
            setAllButtonsInactiveAndOneActive(binding.materialButtonExactDatesActive, binding.materialButtonExactDatesInActive);
            setDatesAllConstraintVisibility(binding.constraintLayoutExactDates);
        });
        binding.materialButtonMonthInActive.setOnClickListener(v -> {
            setAllButtonsInactiveAndOneActive(binding.materialButtonMonthActive, binding.materialButtonMonthInActive);
            setDatesAllConstraintVisibility(binding.constraintLayoutDatesInMonth);
        });
        binding.materialButtonPublicHolidayInActive.setOnClickListener(v -> {
            setAllButtonsInactiveAndOneActive(binding.materialButtonPublicHolidayActive, binding.materialButtonPublicHolidayInActive);
            setDatesAllConstraintVisibility(binding.constraintLayoutDaysOff);
        });
        binding.textViewAddMulticity.setOnClickListener(v -> {
            binding.constraintLayoutMulticity.setVisibility(View.VISIBLE);
            binding.textViewAddMulticity.setVisibility(View.GONE);
            binding.textViewTrip1.setText("Trip 1:");
            setFlyNightBeforeVisibility(false);
            setDirectFlightVisibility(false);
        });
        binding.textViewRemoveMulticity.setOnClickListener(v -> {
            binding.constraintLayoutMulticity.setVisibility(View.GONE);
            binding.textViewAddMulticity.setVisibility(View.VISIBLE);
            binding.textViewTrip1.setText("Trip:");
            setFlyNightBeforeVisibility(true);
        });
        binding.textViewDirectFlight.setOnClickListener(v -> {
            setDirectFlightVisibility(true);
            binding.constraintLayoutMulticity.setVisibility(View.GONE);
            binding.textViewAddMulticity.setVisibility(View.VISIBLE);
            binding.textViewTrip1.setText("Trip:");
        });
        binding.textViewReturnFlight.setOnClickListener(v -> {
            setDirectFlightVisibility(false);
        });
        binding.imageViewRemove.setOnClickListener(v -> {
            addOrRemoveNumberOnTetView(binding.textViewAdultsNumber, false, 1, 9);
        });
        binding.imageViewAdd.setOnClickListener(v -> {
            addOrRemoveNumberOnTetView(binding.textViewAdultsNumber, true, 1, 9);
        });
        binding.imageViewRemoveTotalDays.setOnClickListener(v -> {
            addOrRemoveNumberOnTetView(binding.textViewTotalDaysNumber, false, 1, 25);
        });
        binding.imageViewAddTotalDays.setOnClickListener(v -> {
            addOrRemoveNumberOnTetView(binding.textViewTotalDaysNumber, true, 1, 25);
        });
        binding.imageViewRemoveDaysOff.setOnClickListener(v -> {
            addOrRemoveNumberOnTetView(binding.textViewDaysOffNumber, false, 1, 25);
        });
        binding.imageViewAddDaysOff.setOnClickListener(v -> {
            addOrRemoveNumberOnTetView(binding.textViewDaysOffNumber, true, 1, 25);
        });
        binding.imageViewInfoMixMulticity.setOnClickListener(v -> {
            BottomSheetInfo bottomSheetInfo = new BottomSheetInfo("Mix multicity explanation", "In case that your multicity trip is flexible enough, our algorithm will try changing first destination and second origin spot in order to get better flight deals for you!");
            bottomSheetInfo.show(activity);
        });
        binding.imageViewInfoFlyNightBefore.setOnClickListener(v -> {
            BottomSheetInfo bottomSheetInfo = new BottomSheetInfo("Fly night before explanation", "For those who don't want to spend one more day off on work, we present 'Fly the night before' feature that includes flights from day before but filtered after 18:00h");
            bottomSheetInfo.show(activity);
        });
    }

    private void setAllButtonsInactiveAndOneActive(MaterialButton activeButton, MaterialButton inactiveButton) {
        binding.materialButtonExactDatesActive.setVisibility(View.GONE);
        binding.materialButtonExactDatesInActive.setVisibility(View.VISIBLE);
        binding.materialButtonMonthActive.setVisibility(View.GONE);
        binding.materialButtonMonthInActive.setVisibility(View.VISIBLE);
        binding.materialButtonPublicHolidayActive.setVisibility(View.GONE);
        binding.materialButtonPublicHolidayInActive.setVisibility(View.VISIBLE);
        activeButton.setVisibility(View.VISIBLE);
        inactiveButton.setVisibility(View.VISIBLE);
    }

    private void setDatesAllConstraintVisibility(ConstraintLayout visibleConstraint) {
        binding.constraintLayoutExactDates.setVisibility(View.GONE);
        binding.constraintLayoutDatesInMonth.setVisibility(View.GONE);
        binding.constraintLayoutDaysOff.setVisibility(View.GONE);
        visibleConstraint.setVisibility(View.VISIBLE);
    }

    private void addOrRemoveNumberOnTetView(TextView textView, boolean addOrRemove, int lowBorder, int highBorder) {
        int number = Integer.parseInt(textView.getText().toString());
        int newNumber = addOrRemove ? number + 1 : number - 1;
        if (newNumber >= lowBorder && newNumber <= highBorder) textView.setText(String.valueOf(newNumber));
    }

    private void setFlyNightBeforeVisibility(boolean isVisible) {
        binding.checkBoxFlyNightBefore.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        binding.imageViewInfoFlyNightBefore.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void setDirectFlightVisibility(boolean isDirect) {
        binding.imageViewDate.setVisibility(isDirect ? View.GONE : View.VISIBLE);
        binding.textInputLayoutToDate.setVisibility(isDirect ? View.GONE : View.VISIBLE);
        binding.textViewReturnFlight.setVisibility(isDirect ? View.VISIBLE : View.GONE);
        binding.textViewDirectFlight.setVisibility(isDirect ? View.GONE : View.VISIBLE);
    }
}