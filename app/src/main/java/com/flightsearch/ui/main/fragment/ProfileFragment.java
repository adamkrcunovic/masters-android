package com.flightsearch.ui.main.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentProfileBinding;
import com.flightsearch.ui.intro.activity.IntroActivity;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileFragment extends BaseFragment implements ApplicationConstants {

    @Inject
    MainApplication application;

    @Inject
    FlightSearchServicesApi api;

    private FragmentProfileBinding binding;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        binding = FragmentProfileBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setLoggedOrGuestView();
        setOnClickListeners();
    }

    private void setLoggedOrGuestView() {
        binding.constraintLayoutGuest.setVisibility(application.isUserLogged() ? View.GONE : View.VISIBLE);
        binding.constraintLayoutLogged.setVisibility(application.isUserLogged() ? View.VISIBLE : View.GONE);
    }

    private void setOnClickListeners() {
        binding.materialButtonRegister.setOnClickListener(v -> {
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_BACK.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, false));
        });
        binding.materialButtonSignIn.setOnClickListener(v -> {
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_BACK.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, true));
        });
        binding.materialButtonSignOut.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out?")
                    .setCancelable(false)
                    .setPositiveButton("Sign Out", (dialog, whichButton) -> {
                        activity.showDialog();
                        api.signOut("device").enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                activity.dismissDialog();
                                application.clearUserAuthorizationToken();
                                startActivity(new Intent(activity, IntroActivity.class));
                                activity.finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                                activity.dismissDialog();
                                startActivity(new Intent(activity, IntroActivity.class));
                                activity.finish();
                            }
                        });
                    })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                    }).create();
            alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.app_text));
                }
            });
            alertDialog.show();
        });
        binding.materialButtonAddFriends.setOnClickListener(v -> {
            activity.getNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSearchFriendsFragment());
        });
    }
}