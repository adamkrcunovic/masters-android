package com.flightsearch.utils.network.interceptors;

import com.flightsearch.application.MainApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    private final MainApplication application;

    public HeaderInterceptor(MainApplication application) {
        this.application = application;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (application.isUserLogged()) {
            String authorizationToken = application.userAuthorizationToken();
            builder.addHeader("Authorization", "Bearer " + authorizationToken);
        }
        return chain.proceed(builder.build());
    }
}
