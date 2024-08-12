package com.flightsearch.ui.intro.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.flightsearch.R;
import com.flightsearch.databinding.ActivityIntroBinding;
import com.flightsearch.ui.intro.ViewPagerAdapter;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseActivity;

public class IntroActivity extends BaseActivity {

    private ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.viewPager.setAdapter(new ViewPagerAdapter(this));

        binding.dotsIndicator.attachTo(binding.viewPager);
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.buttonContinueAsGuest.setText(position == 3 ? "Continue as Guest" : "Skip");
            }
        });
        binding.buttonContinueAsGuest.setOnClickListener(v -> {
            if (binding.viewPager.getCurrentItem() == 3) goToMainActivity();
            else binding.viewPager.setCurrentItem(3);
        });
    }

    public void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void goToUserEntryActivity() {
        startActivity(new Intent(this, UserEntryActivity.class));
        finish();
    }

}