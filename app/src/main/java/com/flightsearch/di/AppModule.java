package com.flightsearch.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class AppModule implements ApplicationConstants {

    @Provides
    @Singleton
    protected static SharedPreferences provideSharedPreferences(@ApplicationContext Context application)
    {
        return application.getSharedPreferences(MAIN_SHARE_PREFERENCE, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    protected static MainApplication provideMainApplication(@NotNull @ApplicationContext Context application)
    {
        return (MainApplication) application;
    }

}
