package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.FragmentSearchBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.utils.base.bottomSheet.BottomSheetChooseRadio;
import com.flightsearch.utils.base.bottomSheet.BottomSheetInfo;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.enums.FlightSearchType;
import com.flightsearch.utils.models.helper.DayDTO;
import com.flightsearch.utils.models.helper.MonthDTO;
import com.flightsearch.utils.models.in.InFlightSearchDTO;
import com.flightsearch.utils.models.out.OutAirportDTO;
import com.flightsearch.utils.models.out.OutDatePairsDTO;
import com.flightsearch.utils.models.out.OutFlightDTO;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
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

    @Inject
    MainApplication application;

    private FragmentSearchBinding binding;
    private MainActivity activity;

    private MonthDTO selectedMonth = null;
    private DayDTO selectedDay = null;
    private OutDatePairsDTO selectedDatePair = null;
    private BottomSheetChooseRadio bottomSheetChooseRadioMonth;
    private BottomSheetChooseRadio bottomSheetChooseRadioDay;
    private BottomSheetChooseRadio bottomSheetChooseDatePair;

    private InFlightSearchDTO flightSearchDTO;

    private OutAirportDTO airport1 = null;
    private OutAirportDTO airport2 = null;
    private OutAirportDTO airport3 = null;
    private OutAirportDTO airport4 = null;

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
        setOnFocusChangeListeners();
        setCheckboxStatusChange();
        setTextListeners();
    }

    private void setTextListeners() {
        binding.textInputEditTextFrom1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!textChangedThroughBottomSheet) startSearchHandler(charSequence.toString(), 1000, binding.progressBar1, 1);
                else textChangedThroughBottomSheet = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.textInputEditTextTo1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!textChangedThroughBottomSheet) startSearchHandler(charSequence.toString(), 1000, binding.progressBar2, 2);
                else textChangedThroughBottomSheet = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.textInputEditTextFrom2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!textChangedThroughBottomSheet) startSearchHandler(charSequence.toString(), 1000, binding.progressBar3, 3);
                else textChangedThroughBottomSheet = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.textInputEditTextTo2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!textChangedThroughBottomSheet) startSearchHandler(charSequence.toString(), 1000, binding.progressBar4, 4);
                else textChangedThroughBottomSheet = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private final Handler handlerQueryTextChange = new Handler();
    private Runnable runnableQueryTextChange;
    boolean textChangedThroughBottomSheet = false;
    private void startSearchHandler(String newText, long mills, ProgressBar progressBar, int airportNumber) {
        if (newText == null || newText.length() == 0) {
            progressBar.setVisibility(View.GONE);
            if (airportNumber == 1) airport1 = null;
            if (airportNumber == 2) airport2 = null;
            if (airportNumber == 3) airport3 = null;
            if (airportNumber == 4) airport4 = null;
            handlerQueryTextChange.removeCallbacks(runnableQueryTextChange);
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        handlerQueryTextChange.removeCallbacks(runnableQueryTextChange);
        runnableQueryTextChange = () -> {
            performSearch(newText, progressBar, airportNumber);
        };
        handlerQueryTextChange.postDelayed(runnableQueryTextChange, mills);
    }

    BottomSheetChooseRadio bottomSheetChooseRadio = null;
    private void performSearch(String result, ProgressBar progressBar, int airportNumber) {
        if (result != null && result.length() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            api.getAirports(result).enqueue(new Callback<List<OutAirportDTO>>() {
                @Override
                public void onResponse(Call<List<OutAirportDTO>> call, Response<List<OutAirportDTO>> response) {
                    progressBar.setVisibility(View.GONE);
                    HelperMethods.hideKeyboard(activity);
                    if (response.isSuccessful()) {
                        bottomSheetChooseRadio = new BottomSheetChooseRadio(Collections.singletonList(response.body()), activity, new BottomSheetChooseRadio.OnObjectSelectListener() {
                            @Override
                            public void onObjectSelectChangeListener(Object o) {
                                textChangedThroughBottomSheet = true;
                                OutAirportDTO selectedAirport = (OutAirportDTO) o;
                                if (airportNumber == 1) {
                                    airport1 = selectedAirport;
                                    binding.textInputEditTextFrom1.setText(selectedAirport.getIataCode());
                                }
                                if (airportNumber == 2) {
                                    airport2 = selectedAirport;
                                    binding.textInputEditTextTo1.setText(selectedAirport.getIataCode());
                                }
                                if (airportNumber == 3) {
                                    airport3 = selectedAirport;
                                    binding.textInputEditTextFrom2.setText(selectedAirport.getIataCode());
                                }
                                if (airportNumber == 4) {
                                    airport4 = selectedAirport;
                                    binding.textInputEditTextTo2.setText(selectedAirport.getIataCode());
                                }
                                bottomSheetChooseRadio.dismiss();
                            }
                        }, "Airports", null);
                        bottomSheetChooseRadio.show(activity);
                    }
                }

                @Override
                public void onFailure(Call<List<OutAirportDTO>> call, Throwable throwable) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setCheckboxStatusChange() {
        binding.checkBoxFlyNightBefore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (binding.constraintLayoutDatesInMonth.getVisibility() == View.VISIBLE) {
                        if (binding.radioButtonOneWayFlight.isChecked()) binding.radioGroupMonth.clearCheck();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setPublicHolidayButtonVisibility();
    }

    private void setPublicHolidayButtonVisibility() {
        binding.constraintLayoutButtonPublicHoliday.setVisibility(application.isUserLogged() ? View.VISIBLE : View.GONE);
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
            addOrRemoveNumberOnTetView(binding.textViewTotalDaysNumber, false, 3, 25);
        });
        binding.imageViewAddTotalDays.setOnClickListener(v -> {
            addOrRemoveNumberOnTetView(binding.textViewTotalDaysNumber, true, 3, 25);
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
            binding.checkBoxFlyNightBefore.setChecked(false);
            setFlyNightBeforeVisibility(true);
        });
        binding.materialButtonSearch.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(activity);
            binding.textInputLayoutTo1.setError(null);
            binding.textInputLayoutFrom1.setError(null);
            HelperMethods.setAirportFieldErrors(binding.textInputEditTextFrom1, binding.textInputLayoutFrom1, activity);
            HelperMethods.setAirportFieldErrors(binding.textInputEditTextTo1, binding.textInputLayoutTo1, activity);
            if (binding.textInputLayoutTo1.getError() != null || binding.textInputLayoutFrom1.getError() != null) {
                return;
            }

            if (binding.constraintLayoutMulticity.getVisibility() == View.VISIBLE) {
                binding.textInputLayoutTo2.setError(null);
                binding.textInputLayoutFrom2.setError(null);
                HelperMethods.setAirportFieldErrors(binding.textInputEditTextTo2, binding.textInputLayoutTo2, activity);
                HelperMethods.setAirportFieldErrors(binding.textInputEditTextFrom2, binding.textInputLayoutFrom2, activity);
                if (binding.textInputLayoutTo2.getError() != null || binding.textInputLayoutFrom2.getError() != null) {
                    return;
                }
            }

            if (binding.constraintLayoutExactDates.getVisibility() == View.VISIBLE) {
                binding.textInputLayoutFromDate.setError(null);
                binding.textInputLayoutToDate.setError(null);
                HelperMethods.setDateErrors(binding.textInputEditTextFromDate, binding.textInputLayoutFromDate, activity);
                HelperMethods.setDateErrors(binding.textInputEditTextToDate, binding.textInputLayoutToDate, activity);
                if (binding.textInputLayoutFromDate.getError() != null || binding.textInputLayoutToDate.getError() != null) {
                    return;
                }
                searchFlight();
            }

            if (binding.constraintLayoutDatesInMonth.getVisibility() == View.VISIBLE) {
                if (binding.radioGroupMonth.getCheckedRadioButtonId() != -1) {
                    if (binding.radioButtonDaysCount.isChecked() && selectedDay == null) {
                        //TODO SELECT DAYS
                        return;
                    }
                } else {
                    //TODO SELECT RADIO BUTTON
                    return;
                }
                searchFlight();
            }

            if (binding.constraintLayoutDaysOff.getVisibility() == View.VISIBLE) {
                getDatePairs();
            }
        });
    }

    private void setOnFocusChangeListeners() {
        HelperMethods.setOnFocusChangeListenerAirportField(binding.textInputEditTextFrom1, binding.textInputLayoutFrom1, activity);
        HelperMethods.setOnFocusChangeListenerAirportField(binding.textInputEditTextFrom2, binding.textInputLayoutFrom2, activity);
        HelperMethods.setOnFocusChangeListenerAirportField(binding.textInputEditTextTo1, binding.textInputLayoutTo1, activity);
        HelperMethods.setOnFocusChangeListenerAirportField(binding.textInputEditTextTo2, binding.textInputLayoutTo2, activity);
        HelperMethods.setOnFocusChangeListenerDate(binding.textInputEditTextFromDate, binding.textInputLayoutFromDate, HelperMethods.setTextWatcher(binding.textInputEditTextFromDate), activity);
        HelperMethods.setOnFocusChangeListenerDate(binding.textInputEditTextToDate, binding.textInputLayoutToDate, HelperMethods.setTextWatcher(binding.textInputEditTextToDate), activity);
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
                flightSearchDTO.setDepartureDay(HelperMethods.dateStringToStringBackend(binding.textInputEditTextFromDate.getText().toString()));
                if (binding.textInputLayoutFromDate.getVisibility() == View.VISIBLE) {
                    flightSearchDTO.setReturnDay(HelperMethods.dateStringToStringBackend(binding.textInputEditTextToDate.getText().toString()));
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
        setFlightSearchDTO();
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