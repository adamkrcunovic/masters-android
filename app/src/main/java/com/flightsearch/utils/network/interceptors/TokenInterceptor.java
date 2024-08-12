package com.flightsearch.utils.network.interceptors;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.flightsearch.application.MainApplication;
import com.flightsearch.ui.splash.activity.SplashActivity;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenInterceptor implements Authenticator {

    private final MainApplication application;

    public TokenInterceptor(MainApplication application) {
        this.application = application;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            application.clearUserAuthorizationToken();
            Intent intent = new Intent(application, SplashActivity.class);
            application.startActivity(intent);
            return null;
        }
        return response.request();
    }
}
