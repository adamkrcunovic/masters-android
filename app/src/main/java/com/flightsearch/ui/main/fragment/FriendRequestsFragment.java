package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.FragmentFriendRequestsBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.adapter.RVAdapterFriendSearchItem;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FriendRequestsFragment extends BaseFragment {

    @Inject
    FlightSearchServicesApi api;

    @Inject
    MainApplication application;

    private FragmentFriendRequestsBinding binding;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_requests, container, false);
        binding = FragmentFriendRequestsBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(application.getCurrentUser().getRequests());
        setOnBackPressedListener();
        setLayout();
    }

    public void setLayout() {
        boolean listVisible = application.getCurrentUser().getRequests() != null && !application.getCurrentUser().getRequests().isEmpty();
        binding.constraintLayoutNoRequests.setVisibility(listVisible ? View.GONE : View.VISIBLE);
        binding.recyclerView.setVisibility(listVisible ? View.VISIBLE : View.GONE);
    }

    private void setOnBackPressedListener() {
        binding.materialToolbar.setNavigationOnClickListener(v -> {
            activity.onBackPressed();
        });
    }

    @Override
    public void setAdapter(List<OutUserDTO> foundUsers) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(new RVAdapterFriendSearchItem(foundUsers, application.getCurrentUser(), activity, api, this));
    }
}