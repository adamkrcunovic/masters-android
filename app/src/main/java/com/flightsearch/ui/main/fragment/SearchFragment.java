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
import com.flightsearch.databinding.FragmentSearchBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.utils.base.bottomSheet.BottomSheetChooseRadio;
import com.flightsearch.utils.base.bottomSheet.BottomSheetInfo;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.enums.FlightSearchType;
import com.flightsearch.utils.models.helper.DayDTO;
import com.flightsearch.utils.models.helper.MonthDTO;
import com.flightsearch.utils.models.in.InFlightSearchDTO;
import com.flightsearch.utils.models.out.OutDatePairsDTO;
import com.flightsearch.utils.models.out.OutFlightDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;
import com.google.android.material.button.MaterialButton;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SearchFragment extends Fragment {

    @Inject
    FlightSearchServicesApi api;

    private FragmentSearchBinding binding;
    private MainActivity activity;

    private MonthDTO selectedMonth = null;
    private DayDTO selectedDay = null;
    private OutDatePairsDTO selectedDatePair = null;
    private BottomSheetChooseRadio bottomSheetChooseRadioMonth;
    private BottomSheetChooseRadio bottomSheetChooseRadioDay;
    private BottomSheetChooseRadio bottomSheetChooseDatePair;

    private InFlightSearchDTO flightSearchDTO;

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
            selectedMonth = null;
            selectedDay = null;
            binding.radioButtonDaysCount.setText("Days Count");
            binding.radioGroupMonth.clearCheck();
            showSelectMonthBottomSheet();
        });
        binding.materialButtonMonthChange.setOnClickListener(v -> {
            showSelectMonthBottomSheet();
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
            if (binding.radioButtonOneWayFlight.isChecked()) binding.radioGroupMonth.clearCheck();
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
        binding.radioButtonDaysCount.setOnClickListener(v -> {
            showSelectDaysBottomSheet();
        });
        binding.radioButtonLongWeekend.setOnClickListener(v -> {
            binding.radioButtonDaysCount.setText("Days Count");
        });
        binding.radioButtonDoubleWeekend.setOnClickListener(v -> {
            binding.radioButtonDaysCount.setText("Days Count");
        });
        binding.radioButtonOneWayFlight.setOnClickListener(v -> {
            binding.radioButtonDaysCount.setText("Days Count");
            binding.constraintLayoutMulticity.setVisibility(View.GONE);
            binding.textViewAddMulticity.setVisibility(View.VISIBLE);
            binding.textViewTrip1.setText("Trip:");
            setFlyNightBeforeVisibility(true);
        });
        binding.materialButtonSearch.setOnClickListener(v -> {
            if (binding.constraintLayoutDaysOff.getVisibility() == View.VISIBLE) {
                getDatePairs();
            }
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

    private void showSelectMonthBottomSheet() {
        if (bottomSheetChooseRadioMonth == null) {
            bottomSheetChooseRadioMonth = new BottomSheetChooseRadio(Collections.singletonList(MonthDTO.getNext12Months()), activity, new BottomSheetChooseRadio.OnObjectSelectListener() {
                @Override
                public void onObjectSelectChangeListener(Object o) {
                    selectedMonth = (MonthDTO) o;
                    setAllButtonsInactiveAndOneActive(binding.materialButtonMonthActive, binding.materialButtonMonthInActive);
                    setDatesAllConstraintVisibility(binding.constraintLayoutDatesInMonth);
                    binding.textViewChosenMonth.setText(selectedMonth.monthAndYear);
                    bottomSheetChooseRadioMonth.dismiss();
                }
            }, "Select month", selectedMonth);
        } else {
            bottomSheetChooseRadioMonth.setSelectedItem(selectedMonth);
        }
        bottomSheetChooseRadioMonth.show(activity);
    }

    private void showSelectDaysBottomSheet() {
        if (selectedDay != null) binding.radioButtonDaysCount.setText(selectedDay.dayString + ".\nTap to change!");
        if (bottomSheetChooseRadioDay == null) {
            bottomSheetChooseRadioDay = new BottomSheetChooseRadio(Collections.singletonList(DayDTO.getDays()), activity, new BottomSheetChooseRadio.OnObjectSelectListener() {
                @Override
                public void onObjectSelectChangeListener(Object o) {
                    selectedDay = (DayDTO) o;
                    binding.radioButtonDaysCount.setText(selectedDay.dayString + ".\nTap to change!");
                    bottomSheetChooseRadioDay.dismiss();
                }
            }, "Select number of days", selectedDay);
        } else {
            bottomSheetChooseRadioDay.setSelectedItem(selectedDay);
        }
        bottomSheetChooseRadioDay.show(activity);
    }

    private void setFlightSearchDTO() {
        flightSearchDTO = new InFlightSearchDTO();
        flightSearchDTO.setAdults(Integer.parseInt(binding.textViewAdultsNumber.getText().toString()));
        if (binding.constraintLayoutExactDates.getVisibility() == View.VISIBLE || binding.constraintLayoutDaysOff.getVisibility() == View.VISIBLE) {
            flightSearchDTO.setFlightSearchType(FlightSearchType.ExactDate.ordinal());
            if (binding.constraintLayoutExactDates.getVisibility() == View.VISIBLE) {
                flightSearchDTO.setDepartureDay(HelperMethods.dateStringToStringBackend(binding.textInputEditTextToDate.getText().toString()));
                if (binding.textInputLayoutFromDate.getVisibility() == View.VISIBLE) {
                    flightSearchDTO.setReturnDay(HelperMethods.dateStringToStringBackend(binding.textInputEditTextFromDate.getText().toString()));
                }
            }
            if (binding.constraintLayoutDaysOff.getVisibility() == View.VISIBLE) {
                flightSearchDTO.setDepartureDay(selectedDatePair.getStartDate());
                flightSearchDTO.setReturnDay(selectedDatePair.getEndDate());
            }
        }
        if (binding.constraintLayoutDatesInMonth.getVisibility() == View.VISIBLE) {
            flightSearchDTO.setMonth(selectedMonth.month + 1);
            flightSearchDTO.setYear(selectedMonth.year);
            if (binding.radioButtonOneWayFlight.isChecked()) {
                flightSearchDTO.setFlightSearchType(FlightSearchType.MonthDirectFlight.ordinal());
            }
            if (binding.radioButtonDaysCount.isChecked()) {
                flightSearchDTO.setFlightSearchType(FlightSearchType.DurationInMonth.ordinal());
                flightSearchDTO.setTripDuration(selectedDay.count);
            }
            if (binding.radioButtonLongWeekend.isChecked()) {
                flightSearchDTO.setFlightSearchType(FlightSearchType.LongWeekendInMonth.ordinal());
            }
            if (binding.radioButtonDoubleWeekend.isChecked()) {
                flightSearchDTO.setFlightSearchType(FlightSearchType.DoubleLongWeekendInMonth.ordinal());
            }
        }
        flightSearchDTO.setFromAirport(binding.textInputEditTextFrom1.getText().toString());
        if (binding.constraintLayoutMulticity.getVisibility() == View.VISIBLE) {
            flightSearchDTO.setMultiCity1(binding.textInputEditTextTo1.getText().toString());
            flightSearchDTO.setMultiCity2(binding.textInputEditTextFrom2.getText().toString());
            flightSearchDTO.setToAirport(binding.textInputEditTextTo2.getText().toString());
            flightSearchDTO.setMixMultiCity(binding.checkBoxMixMulticity.isChecked());
        } else {
            flightSearchDTO.setToAirport(binding.textInputEditTextTo1.getText().toString());
            flightSearchDTO.setFlyTheNightBefore(binding.checkBoxFlyNightBefore.isChecked());
        }
    }

    private void getDatePairs() {
        activity.showDialog();
        api.getDatePairs(Integer.parseInt(binding.textViewTotalDaysNumber.getText().toString()), Integer.parseInt(binding.textViewDaysOffNumber.getText().toString())).enqueue(new Callback<List<OutDatePairsDTO>>() {
            @Override
            public void onResponse(Call<List<OutDatePairsDTO>> call, Response<List<OutDatePairsDTO>> response) {
                activity.dismissDialog();
                if (response.isSuccessful()) {
                    bottomSheetChooseDatePair = new BottomSheetChooseRadio(Collections.singletonList(response.body()), activity, new BottomSheetChooseRadio.OnObjectSelectListener() {
                        @Override
                        public void onObjectSelectChangeListener(Object o) {
                            selectedDatePair = (OutDatePairsDTO) o;
                            bottomSheetChooseDatePair.dismiss();
                            setFlightSearchDTO();
                            searchFlight();
                        }
                    }, "Select date pair", null);
                    bottomSheetChooseDatePair.show(activity);
                }
            }

            @Override
            public void onFailure(Call<List<OutDatePairsDTO>> call, Throwable throwable) {
                activity.dismissDialog();
            }
        });
    }

    private void searchFlight() {
        activity.showDialog();
        System.out.println(flightSearchDTO.toString());
        api.getFlightDeals(flightSearchDTO).enqueue(new Callback<OutFlightDTO>() {
            @Override
            public void onResponse(Call<OutFlightDTO> call, Response<OutFlightDTO> response) {
                activity.dismissDialog();
                if (response.isSuccessful()) {
                    activity.getNavController().navigate(SearchFragmentDirections.actionSearchFragmentToFlightSearchResultFragment(response.body()));
                }
            }

            @Override
            public void onFailure(Call<OutFlightDTO> call, Throwable throwable) {
                activity.dismissDialog();
            }
        });
    }
}