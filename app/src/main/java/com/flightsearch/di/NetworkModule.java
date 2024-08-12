package com.flightsearch.di;

import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.utils.network.interceptors.HeaderInterceptor;
import com.flightsearch.utils.network.interceptors.TokenInterceptor;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(SingletonComponent.class)
@Module
public class NetworkModule implements ApplicationConstants {

    @Singleton
    @Provides
    protected static String providesBaseUrl() {
        return BASE_SERVER_URL;
    }

    @Singleton
    @Provides
    protected static HeaderInterceptor HeaderInterceptor(MainApplication application) {
        return new HeaderInterceptor(application);
    }

    @Singleton
    @Provides
    protected static TokenInterceptor providesTokenAuth(MainApplication application) {
        return new TokenInterceptor(application);
    }

    @Singleton
    @Provides
    protected static Gson providesGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    protected static Converter.Factory providesConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }


    @Singleton
    @Provides
    protected static OkHttpClient providesOkHttpClient(HeaderInterceptor headerInterceptor, TokenInterceptor tokenInterceptor) {
        @NotNull OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .authenticator(tokenInterceptor)
                .addInterceptor(headerInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS);
        return httpClient.build();
    }

    @Singleton
    @Provides
    protected static Retrofit providesRetrofitClient(
            OkHttpClient okHttpClient,
            String baseUrl,
            Converter.Factory converterFactory) {

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .build();
    }

    @Singleton
    @Provides
    protected static FlightSearchServicesApi providesFlightSearchServicesApi(Retrofit retrofit) {
        return retrofit.create(FlightSearchServicesApi.class);
    }
}
