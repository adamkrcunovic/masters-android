package com.flightsearch.ui.splash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.ActivitySplashBinding;
import com.flightsearch.ui.intro.activity.IntroActivity;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseActivity;
import com.flightsearch.utils.models.in.InRegisterDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SplashActivity extends BaseActivity {

    @Inject
    FlightSearchServicesApi api;

    @Inject
    MainApplication application;

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setOnCLickListeners();
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
                        startActivity(new Intent(SplashActivity.this, UserEntryActivity.class));
                    }
                }
                finish();
            }
        }, 2000);
    }

    private void setOnCLickListeners() {
        /*binding.button.setOnClickListener(v -> {
            InRegisterDTO inRegisterDTO = new InRegisterDTO();
            inRegisterDTO.setEmail("aa@aa.com");
            inRegisterDTO.setPassword("Adam123!");
            inRegisterDTO.setName("Aa");
            inRegisterDTO.setLastName("Bb");
            inRegisterDTO.setBirthday("1997-12-12");
            inRegisterDTO.setDeviceId("device");
            inRegisterDTO.setCountryId(1);
            inRegisterDTO.setPreferences("adventurous-1;");
            api.register(inRegisterDTO).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        System.out.println(response.code());
                    }
                    else {
                        System.out.println("YEEY");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                    System.out.println("I DIDN'T IT!");
                }
            });
        });*/
    }


}