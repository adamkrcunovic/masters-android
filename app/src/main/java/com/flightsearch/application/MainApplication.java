package com.flightsearch.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.utils.models.out.OutCountryDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltAndroidApp
public class MainApplication extends Application implements ApplicationConstants {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    FlightSearchServicesApi api;

    private List<OutCountryDTO> allCountries;

    public boolean isUserLogged() {
        return sharedPreferences.getString(USER_AUTHORIZATION_TOKEN, null) != null;
    }

    public String userAuthorizationToken() {
        return sharedPreferences.getString(USER_AUTHORIZATION_TOKEN, null);
    }

    public void clearUserAuthorizationToken() {
        sharedPreferences.edit().remove(USER_AUTHORIZATION_TOKEN).apply();
    }

    public void storeUserAuthorizationToken(String token) {
        sharedPreferences.edit().putString(USER_AUTHORIZATION_TOKEN, token).apply();
    }

    public boolean isUserFirstTimeAppOpening() {
        return sharedPreferences.getBoolean(APP_FIRST_TIME_OPENING, true);
    }

    public void setIntroOpened() {
        sharedPreferences.edit().putBoolean(APP_FIRST_TIME_OPENING, false).apply();
    }

    public List<OutCountryDTO> getAllCountries() {
        return allCountries;
    }

    public void getAllCountriesApiCall() {
        api.getCountries().enqueue(new Callback<List<OutCountryDTO>>() {
            @Override
            public void onResponse(Call<List<OutCountryDTO>> call, Response<List<OutCountryDTO>> response) {
                if (response.isSuccessful()) {
                    allCountries = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<OutCountryDTO>> call, Throwable throwable) {

            }
        });
    }
}
