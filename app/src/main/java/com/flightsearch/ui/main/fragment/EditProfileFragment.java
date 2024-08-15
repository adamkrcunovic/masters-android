package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.FragmentEditProfileBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.utils.base.bottomSheet.BottomSheetChooseRadio;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.in.InEditProfileDTO;
import com.flightsearch.utils.models.out.OutCountryDTO;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class EditProfileFragment extends Fragment {

    @Inject
    FlightSearchServicesApi api;

    @Inject
    MainApplication application;

    private FragmentEditProfileBinding binding;
    private MainActivity activity;
    private TextWatcher tw;

    private OutCountryDTO selectedCountry;
    private OutUserDTO user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        binding = FragmentEditProfileBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUserData();
        setTextWatcher();
        setOnFocusChangeListeners();
        setOnClickListeners();
    }

    private void setTextWatcher() {
        tw = HelperMethods.setTextWatcher(binding.textInputEditTextBirthday);
    }

    private void setUserData() {
        user = application.getCurrentUser();
        List<OutCountryDTO> countries = application.getAllCountries();
        for (OutCountryDTO country: countries
             ) {
            if (user.getCountry().equals(country.getCountryName())) {
                selectedCountry = country;
                break;
            }
        }
        binding.textInputEditTextFirstName.setText(user.getName());
        binding.textInputEditTextLastName.setText(user.getLastName());
        binding.textInputEditTextEmail.setText(user.getEmail());
        binding.textInputEditTextBirthday.setText(HelperMethods.BackendStringToDateString(user.getBirthday()));
        binding.textInputEditTextCountry.setText(user.getCountry());
        binding.textInputEditTextPreferences.setText(user.getPreferences());
    }

    private void setOnFocusChangeListeners() {
        binding.textInputEditTextFirstName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutFirstName.setError(null);
            } else {
                setFirstNameErrors();
            }
        });
        binding.textInputEditTextLastName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutLastName.setError(null);
            } else {
                setLastNameErrors();
            }
        });
        binding.textInputEditTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutEmail.setError(null);
            } else {
                setEmailErrors();
            }
        });
        binding.textInputEditTextBirthday.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutBirthday.setError(null);
                binding.textInputEditTextBirthday.addTextChangedListener(tw);
                if (binding.textInputEditTextBirthday.getText().toString().length() == 0) {
                    binding.textInputEditTextBirthday.setText("mm/dd/yyyy");
                    binding.textInputEditTextBirthday.post(() -> binding.textInputEditTextBirthday.setSelection(0));
                }
            } else {
                binding.textInputEditTextBirthday.removeTextChangedListener(tw);
                if (binding.textInputEditTextBirthday.getText().toString().equals("mm/dd/yyyy")) binding.textInputEditTextBirthday.setText("");
                setBirthdayErrors();
            }
        });
    }

    private void setFirstNameErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextFirstName.getText())) binding.textInputLayoutFirstName.setError(activity.getString(R.string.error_field_required));
    }

    private void setLastNameErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextLastName.getText())) binding.textInputLayoutLastName.setError(activity.getString(R.string.error_field_required));
    }

    private void setEmailErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextEmail.getText())) binding.textInputLayoutEmail.setError(activity.getString(R.string.error_field_required));
        else if (!HelperMethods.isValidEmail(binding.textInputEditTextEmail.getText().toString())) binding.textInputLayoutEmail.setError(activity.getString(R.string.error_invalid_email));
    }

    private void setBirthdayErrors() {
        if (!TextUtils.isEmpty(binding.textInputEditTextBirthday.getText()) && !HelperMethods.isValidBirthday(binding.textInputEditTextBirthday.getText().toString())) binding.textInputLayoutBirthday.setError(activity.getString(R.string.error_invalid_birthday_date));
    }

    private BottomSheetChooseRadio bottomSheetChooseRadio;
    private void setOnClickListeners() {
        binding.textInputEditTextCountry.setOnClickListener(v -> {
            if (bottomSheetChooseRadio == null) {
                bottomSheetChooseRadio = new BottomSheetChooseRadio(Collections.singletonList(application.getAllCountries()), activity, new BottomSheetChooseRadio.OnObjectSelectListener() {
                    @Override
                    public void onObjectSelectChangeListener(Object o) {
                        selectedCountry = (OutCountryDTO) o;
                        binding.textInputLayoutCountry.setError(null);
                        binding.textInputEditTextCountry.setText(selectedCountry.getCountryName());
                        bottomSheetChooseRadio.dismiss();
                    }
                }, "Select country", selectedCountry);
            } else {
                bottomSheetChooseRadio.setSelectedItem(selectedCountry);
            }
            bottomSheetChooseRadio.show(activity);
        });
        binding.materialButtonConfirm.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(activity);
            if (!isFormValid()) {
                //activity.showSnackBar(activity.getString(R.string.error_invalid_form), MySnackbar.SnackbarType.ERROR);
                return;
            }
            callEditProfileApi();
        });
    }

    private boolean isFormValid() {
        setFirstNameErrors();
        setLastNameErrors();
        setEmailErrors();
        setBirthdayErrors();
        return binding.textInputLayoutFirstName.getError() == null
                && binding.textInputLayoutLastName.getError() == null
                && binding.textInputLayoutEmail.getError() == null
                && binding.textInputLayoutBirthday.getError() == null;
    }

    private void callEditProfileApi() {
        String newName = binding.textInputEditTextFirstName.getText().toString();
        String newLastName = binding.textInputEditTextLastName.getText().toString();
        String newEmail = binding.textInputEditTextEmail.getText().toString();
        String newBirthday = HelperMethods.dateStringToStringBackend(binding.textInputEditTextBirthday.getText().toString());
        String newCountry = selectedCountry.getCountryName();
        String newPreferences = binding.textInputEditTextPreferences.getText().toString();

        InEditProfileDTO inEditProfileDTO = new InEditProfileDTO();

        if (!newName.equals(user.getName())) inEditProfileDTO.setName(newName);
        if (!newLastName.equals(user.getLastName())) inEditProfileDTO.setLastName(newLastName);
        if (!newEmail.equals(user.getEmail())) inEditProfileDTO.setEmail(newEmail);
        if (!newBirthday.equals(user.getBirthday())) inEditProfileDTO.setBirthday(newBirthday);
        if (!newCountry.equals(user.getCountry())) inEditProfileDTO.setCountryId(selectedCountry.getId());
        if (!newPreferences.equals(user.getPreferences())) inEditProfileDTO.setPreferences(newPreferences);

        if (!inEditProfileDTO.areAllDataNull()) {
            inEditProfileDTO.setCountryId(selectedCountry.getId());
            activity.showDialog();
            api.editProfile(inEditProfileDTO).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    activity.dismissDialog();
                    if (response.isSuccessful()) {
                        activity.onBackPressed();
                        application.getUser();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    activity.dismissDialog();
                }
            });
        }
    }
}