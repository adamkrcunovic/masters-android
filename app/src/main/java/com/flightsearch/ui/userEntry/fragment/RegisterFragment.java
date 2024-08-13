package com.flightsearch.ui.userEntry.fragment;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentRegisterBinding;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.bottomSheet.BottomSheetChooseRadio;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.helpers.PasswordStrengthCalculatorHelper;
import com.flightsearch.utils.models.in.InRegisterDTO;
import com.flightsearch.utils.models.out.OutCountryDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;
import java.util.Calendar;
import java.util.Collections;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RegisterFragment extends Fragment implements ApplicationConstants {

    @Inject
    FlightSearchServicesApi api;

    @Inject
    MainApplication application;

    private InRegisterDTO inRegisterDTO;

    private FragmentRegisterBinding binding;
    private UserEntryActivity activity;

    private PasswordStrengthCalculatorHelper passwordStrengthCalculatorHelper;
    private TextWatcher tw;
    private Boolean hasPasswordFocus = true;

    private OutCountryDTO selectedCountry = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        binding = FragmentRegisterBinding.bind(view);
        activity = (UserEntryActivity) getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callCountries();
        setPasswordHelper();
        setTextWatcher();
        setOnFocusChangeListeners();
        setOnClickListeners();
        setPasswordStrengthColorsListener();
    }

    private void callCountries() {
        application.getAllCountriesApiCall();
    }

    private void setPasswordHelper() {
        passwordStrengthCalculatorHelper = new PasswordStrengthCalculatorHelper();
        binding.textInputEditTextPassword.addTextChangedListener(passwordStrengthCalculatorHelper);
    }

    private void setTextWatcher() {
        tw = new TextWatcher() {
            private String current = "";
            private String mmddyyyy = "mmddyyyy";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Calendar cal = Calendar.getInstance();

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + mmddyyyy.substring(clean.length());
                    } else {
                        // This part makes sure that when we finish entering numbers
                        // the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(2, 4));
                        int mon = Integer.parseInt(clean.substring(0, 2));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? (cal.get(Calendar.MONTH) + 1) : mon;
                        cal.set(Calendar.MONTH, mon - 1);

                        year = (year < 1900) ? 1900 : Math.min(year, cal.get(Calendar.YEAR));
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = day < 1 ? 1 : (day > cal.getActualMaximum(Calendar.DATE)) ? cal.get(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", mon, day, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2), clean.substring(2, 4), clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    binding.textInputEditTextBirthday.setText(current);
                    binding.textInputEditTextBirthday.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
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
        binding.textInputEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutPassword.setError(null);
                binding.textInputLayoutConfirmPassword.setError(null);
            } else {
                hasPasswordFocus = false;
                setPasswordStrengthColorsByForce();
                setPasswordErrors();
            }
        });
        binding.textInputEditTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutConfirmPassword.setError(null);
            } else {
                setConfirmPasswordErrors();
            }
        });
    }

    private void setFirstNameErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextFirstName.getText())) binding.textInputLayoutFirstName.setError(activity.getString(R.string.error_field_required));
    }

    private void setLastNameErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextLastName.getText())) binding.textInputLayoutLastName.setError(activity.getString(R.string.error_field_required));
    }

    private void setCountryErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextCountry.getText())) binding.textInputLayoutCountry.setError(activity.getString(R.string.error_field_required));
    }

    private void setEmailErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextEmail.getText())) binding.textInputLayoutEmail.setError(activity.getString(R.string.error_field_required));
        else if (!HelperMethods.isValidEmail(binding.textInputEditTextEmail.getText().toString())) binding.textInputLayoutEmail.setError(activity.getString(R.string.error_invalid_email));
    }

    private void setBirthdayErrors() {
        if (!TextUtils.isEmpty(binding.textInputEditTextBirthday.getText()) && !HelperMethods.isValidBirthday(binding.textInputEditTextBirthday.getText().toString())) binding.textInputLayoutBirthday.setError(activity.getString(R.string.error_invalid_birthday_date));
    }

    private void setPasswordErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextPassword.getText())) binding.textInputLayoutPassword.setError(activity.getString(R.string.error_field_required));
        else if (!passwordStrengthCalculatorHelper.meetsAllRequirements()) binding.textInputLayoutPassword.setError(activity.getString(R.string.error_password_requirements));
    }

    private void setConfirmPasswordErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextConfirmPassword.getText())) binding.textInputLayoutConfirmPassword.setError(activity.getString(R.string.error_field_required));
        else if (!binding.textInputEditTextPassword.getText().toString().equals(binding.textInputEditTextConfirmPassword.getText().toString())) binding.textInputLayoutConfirmPassword.setError(activity.getString(R.string.error_password_mismatch));
    }

    private void setPasswordStrengthColorsListener() {
        passwordStrengthCalculatorHelper.getNumberCase().observe(activity, value -> {
            displayPasswordSuggestions(value, binding.textView8Cars, hasPasswordFocus);
        });
        passwordStrengthCalculatorHelper.getDigit().observe(activity, value -> {
            displayPasswordSuggestions(value, binding.textView123, hasPasswordFocus);
        });
        passwordStrengthCalculatorHelper.getLowerCase().observe(activity, value ->{
            displayPasswordSuggestions(value, binding.textViewAbcLow, hasPasswordFocus);
        });
        passwordStrengthCalculatorHelper.getUpperCase().observe(activity, value ->{
            displayPasswordSuggestions(value, binding.textViewAbcUp, hasPasswordFocus);
        });
        passwordStrengthCalculatorHelper.getSpecialChar().observe(activity, value ->{
            displayPasswordSuggestions(value, binding.textViewSpecCar, hasPasswordFocus);
        });
    }

    private void setPasswordStrengthColorsByForce() {
        displayPasswordSuggestions(passwordStrengthCalculatorHelper.getNumberCase().getValue(), binding.textView8Cars);
        displayPasswordSuggestions(passwordStrengthCalculatorHelper.getDigit().getValue(), binding.textView123);
        displayPasswordSuggestions(passwordStrengthCalculatorHelper.getLowerCase().getValue(), binding.textViewAbcLow);
        displayPasswordSuggestions(passwordStrengthCalculatorHelper.getUpperCase().getValue(), binding.textViewAbcUp);
        displayPasswordSuggestions(passwordStrengthCalculatorHelper.getSpecialChar().getValue(), binding.textViewSpecCar);
    }

    private static void displayPasswordSuggestions(Integer value, TextView textView) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, value == PASSWORD_STRENGTH_CORRECT ? R.drawable.ic_check : R.drawable.ic_not_check);
        setTextViewDrawableColor(textView, value == PASSWORD_STRENGTH_CORRECT ? R.color.color_password_strength_good : R.color.color_password_strength_bad);
    }

    private static void displayPasswordSuggestions(Integer value, TextView textView, boolean hasFocus) {
        if (value == PASSWORD_STRENGTH_CORRECT){
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_check);
            setTextViewDrawableColor(textView,  R.color.color_password_strength_good);
        } else {
            if (value == PASSWORD_STRENGTH_INCORRECT && (!hasFocus || textView.getCompoundDrawables()[3] != null)) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,  R.drawable.ic_not_check);
                setTextViewDrawableColor(textView,  R.color.color_password_strength_bad);
            }
        }
    }

    public static void setTextViewDrawableColor(TextView textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(textView.getContext(), color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    private boolean isFormValid() {
        setFirstNameErrors();
        setLastNameErrors();
        setEmailErrors();
        setBirthdayErrors();
        setPasswordErrors();
        setConfirmPasswordErrors();
        setCountryErrors();
        return binding.textInputLayoutFirstName.getError() == null
                && binding.textInputLayoutLastName.getError() == null
                && binding.textInputLayoutEmail.getError() == null
                && binding.textInputLayoutBirthday.getError() == null
                && binding.textInputLayoutPassword.getError() == null
                && binding.textInputLayoutConfirmPassword.getError() == null
                && binding.textInputLayoutCountry.getError() == null;
    }

    BottomSheetChooseRadio bottomSheetChooseRadio;
    private void setOnClickListeners() {
        binding.textInputEditTextCountry.setOnClickListener(v -> {
            setCountryErrors();
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
        binding.materialButtonRegister.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(activity);
            setPasswordStrengthColorsByForce();
            if (!isFormValid()) {
                //activity.showSnackBar(activity.getString(R.string.error_invalid_form), MySnackbar.SnackbarType.ERROR);
                return;
            }
            registerAccountRequest();
        });
    }

    private void setInRegisterDTO() {
        inRegisterDTO = new InRegisterDTO();
        inRegisterDTO.setName(binding.textInputEditTextFirstName.getText().toString());
        inRegisterDTO.setLastName(binding.textInputEditTextLastName.getText().toString());
        inRegisterDTO.setEmail(binding.textInputEditTextEmail.getText().toString());
        inRegisterDTO.setBirthday(HelperMethods.dateStringToStringBackend(binding.textInputEditTextBirthday.getText().toString()));
        inRegisterDTO.setPassword(binding.textInputEditTextPassword.getText().toString());
        // DEVICE, COUNTRY, PREFERENCE!!!
        inRegisterDTO.setCountryId(1);
        inRegisterDTO.setDeviceId("IDD");
        inRegisterDTO.setPreferences("IDD;");
    }

    private void registerAccountRequest() {
        activity.showDialog();
        setInRegisterDTO();
        api.register(inRegisterDTO).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                activity.dismissDialog();
                System.out.println(response.body());
                if (response.isSuccessful()) {
                    activity.navigateToNextScreen();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                activity.dismissDialog();
            }
        });
    }

}