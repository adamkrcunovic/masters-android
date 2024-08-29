package com.flightsearch.ui.intro.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentIntroPictureBinding;
import com.flightsearch.ui.intro.activity.IntroActivity;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.helpers.HelperMethods;

public class IntroPictureFragment extends BaseFragment implements ApplicationConstants {

    private FragmentIntroPictureBinding binding;
    private int imageIndex;
    IntroActivity activity;

    public static IntroPictureFragment newInstance(int imageIndex) {
        Bundle args = new Bundle();
        args.putInt(IMAGE_INDEX, imageIndex);
        IntroPictureFragment fragment = new IntroPictureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIntroPictureBinding.inflate(inflater, container, false);
        activity = (IntroActivity) getActivity();
        imageIndex = getArguments().getInt(IMAGE_INDEX);
        setImage();
        setOnClickListeners();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setLayout();
    }

    private void setLayout() {
        if (imageIndex == 4) {
            boolean notificationsEnabled = ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
            binding.materialButtonEnableNotification.setVisibility(notificationsEnabled ? View.GONE : View.VISIBLE);
            binding.textViewNotificationsEnabled.setVisibility(notificationsEnabled ? View.VISIBLE : View.GONE);
        }
    }

    int buttonClickCounter = 0;
    private void setOnClickListeners() {
        binding.materialButtonEnableNotification.setOnClickListener(v -> {
            buttonClickCounter++;
            if (buttonClickCounter <= 1) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 0);
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(activity)
                        .setTitle("You currently have push notifications disabled.")
                        .setMessage("\nTo change your device notification settings:\n\n" +
                                "1. Click the button \"Open my settings\", or navigate to the app withing your device settings\n\n" +
                                "2. Click Notifications to then toggle your notification preference\n")
                        .setCancelable(false)
                        .setPositiveButton("Open My Settings", (dialog, whichButton) -> {
                            startActivity(
                                    new Intent(
                                            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", activity.getPackageName(), null)
                                    )
                            );
                        })
                        .setNegativeButton("Cancel", (dialog, whichButton) -> {
                        }).create();
                alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(activity.getResources().getColor(R.color.app_text));
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void setImage() {
        int resourceId = getContext().getResources().getIdentifier("intro_" + imageIndex, "drawable", getContext().getPackageName());
        binding.imageView.setImageResource(resourceId);
    }
}