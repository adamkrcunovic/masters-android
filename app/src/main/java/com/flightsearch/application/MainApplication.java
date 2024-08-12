package com.flightsearch.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.flightsearch.constants.ApplicationConstants;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MainApplication extends Application implements ApplicationConstants {

    @Inject
    SharedPreferences sharedPreferences;

    public boolean isUserLogged() {
        return sharedPreferences.getString(USER_AUTHORIZATION_TOKEN, null) != null;
    }

    public String userAuthorizationToken() {
        return sharedPreferences.getString(USER_AUTHORIZATION_TOKEN, null);
    }

    public void clearUserAuthorizationToken() {
        sharedPreferences.edit().remove(USER_AUTHORIZATION_TOKEN).apply();
    }

    public boolean isUserFirstTimeAppOpening() {
        boolean firstTimeOpening = sharedPreferences.getBoolean(APP_FIRST_TIME_OPENING, true);
        if (firstTimeOpening) {
            sharedPreferences.edit().putBoolean(APP_FIRST_TIME_OPENING, false).apply();
        }
        return firstTimeOpening;
    }

}
