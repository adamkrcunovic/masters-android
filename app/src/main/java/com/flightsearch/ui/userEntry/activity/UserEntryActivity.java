package com.flightsearch.ui.userEntry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.flightsearch.R;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.ActivityUserEntryBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.userEntry.ViewPagerAdapter;
import com.flightsearch.utils.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserEntryActivity extends BaseActivity implements ApplicationConstants {

    private ActivityUserEntryBinding binding;
    private ViewPagerAdapter adapter;
    private boolean inversePages;

    public NavigationMode navigationMode = NavigationMode.GO_TO_HOME;

    public enum NavigationMode {

        GO_TO_HOME(0),
        GO_BACK(1);

        private int value;

        NavigationMode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserEntryBinding.inflate(getLayoutInflater());
        getArguments();
        setTabLayout();
        setViewPager();
        setToolBarListener();
        setContentView(binding.getRoot());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToNextScreen();
    }

    private void getArguments() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int navigateValue = bundle.getInt(NEXT_USER_ENTRY_PAGE);
            switch (navigateValue) {
                case 0:
                    navigationMode = NavigationMode.GO_TO_HOME;
                    break;
                case 1:
                    navigationMode = NavigationMode.GO_BACK;
                    break;
                default:
                    break;
            }
            inversePages = bundle.getBoolean(INVERSE_USER_ENTRY_PAGES);
        }
    }

    private void setToolBarListener() {
        binding.materialToolbar.setNavigationOnClickListener(v -> {
            navigateToNextScreen();
        });
    }

    private void setTabLayout() {
        if (!inversePages) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("REGISTER"));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("SIGN IN"));
        } else {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("SIGN IN"));
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("REGISTER"));
        }
        binding.tabLayout.setTabMode(TabLayout.MODE_FIXED);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                binding.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setViewPager() {
        adapter = new ViewPagerAdapter(this);
        adapter.setInversePages(inversePages);
        binding.viewPager.setAdapter(adapter);

        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.getTabAt(position).select();
            }
        });
    }

    public void navigateToNextScreen() {
        Log.i("TAG", "navigateToNextScreen: NAVIGATION MODE: " + navigationMode);
        switch (navigationMode) {
            case GO_TO_HOME:
                goToMainActivity();
                break;
            case GO_BACK:
                goBack();
                break;
            default:
                break;
        }
    }

    public void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void goBack() {
        finish();
    }
}