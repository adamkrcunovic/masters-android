package com.flightsearch.ui.userEntry.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.FragmentSignInBinding;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseActivity;
import com.flightsearch.utils.firebase.MyFirebaseMessagingService;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.in.InLoginDTO;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SignInFragment extends Fragment {

    @Inject
    FlightSearchServicesApi api;

    @Inject
    MainApplication application;

    @Inject
    SharedPreferences sharedPreferences;

    private FragmentSignInBinding binding;
    private UserEntryActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        binding = FragmentSignInBinding.bind(view);
        activity = (UserEntryActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();
        setOnFocisChangeListeners();
    }

    private void setOnClickListeners() {
        binding.materialButtonSignIn.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(activity);
            setEmailErrors();
            setPasswordErrors();
            if (binding.textInputLayoutEmail.getError() == null && binding.textInputLayoutPassword.getError() == null) {
                callSignIn();
            }
        });
    }

    private void setOnFocisChangeListeners() {
        binding.textInputEditTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutEmail.setError(null);
            } else {
                setEmailErrors();
            }
        });

        binding.textInputEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutPassword.setError(null);
            } else {
                setPasswordErrors();
            }
        });
    }

    private void setEmailErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextEmail.getText())) binding.textInputLayoutEmail.setError(activity.getString(R.string.error_field_required));
        else if (!HelperMethods.isValidEmail(binding.textInputEditTextEmail.getText().toString())) binding.textInputLayoutEmail.setError(activity.getString(R.string.error_invalid_email));
    }

    private void setPasswordErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextPassword.getText())) binding.textInputLayoutPassword.setError(activity.getString(R.string.error_field_required));
    }

    private void callSignIn() {
        activity.showDialog();
        api.signIn(new InLoginDTO(binding.textInputEditTextEmail.getText().toString(), binding.textInputEditTextPassword.getText().toString(), MyFirebaseMessagingService.getDeviceId(sharedPreferences))).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    application.storeUserAuthorizationToken(response.body());
                    api.getUserData().enqueue(new Callback<OutUserDTO>() {
                        @Override
                        public void onResponse(Call<OutUserDTO> call, Response<OutUserDTO> response) {
                            activity.dismissDialog();
                            if (response.isSuccessful()) {
                                application.setCurrentUser(response.body());
                                activity.navigateToNextScreen();
                            }
                        }

                        @Override
                        public void onFailure(Call<OutUserDTO> call, Throwable throwable) {
                            activity.dismissDialog();
                        }
                    });
                } else {
                    activity.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                activity.dismissDialog();
            }
        });
    }

}