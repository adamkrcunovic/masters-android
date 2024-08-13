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
        builder.addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiI4YzFlYTA4MC03MDEzLTRiZjItOWJmMy0xOTlkZDNlM2Y2OTEiLCJDb3VudHJ5SWQiOiIxIiwibmJmIjoxNzIzNTgzMTgxLCJleHAiOjE3MzE1MzU1ODEsImlhdCI6MTcyMzU4MzE4MSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo1MTkyIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo1MTkyIn0.Yg1CcU5pW_dAr6tIhL8-38Eu0EsCNe9E_tOYp6s1QLzgukYfkhuRY6l-OsD0TiqtfTDTxIwrVo3XTLgg_FV7HA");
        if (application.isUserLogged()) {
            String authorizationToken = application.userAuthorizationToken();
            builder.addHeader("Authorization", "Bearer " + authorizationToken);
        }
        return chain.proceed(builder.build());
    }
}
