package com.flightsearch.ui.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.ActivitySplashBinding;
import com.flightsearch.ui.intro.activity.IntroActivity;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseActivity;
import com.flightsearch.utils.firebase.BearerTokenGoogle;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashActivity extends BaseActivity implements ApplicationConstants {

    @Inject
    FlightSearchServicesApi api;

    @Inject
    MainApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        findViewById(R.id.imageView_logo).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom));

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (application.isUserLogged()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    if (application.isUserFirstTimeAppOpening()) {
                        startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    } else {
                        startActivity((new Intent(SplashActivity.this, UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_TO_HOME.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, true));
                    }
                }
                finish();
            }
        }, 2000);
        application.getAllCountriesApiCall();
        BearerTokenGoogle.context = application.getApplicationContext();
    }

}