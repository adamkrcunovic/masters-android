package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.FragmentSearchFriendsBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.adapter.RVAdapterFriendSearchItem;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SearchFriendsFragment extends BaseFragment {

    @Inject
    FlightSearchServicesApi api;

    @Inject
    MainApplication application;

    private FragmentSearchFriendsBinding binding;

    private final Handler handlerQueryTextChange = new Handler();
    private Runnable runnableQueryTextChange;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_friends, container, false);
        binding = FragmentSearchFriendsBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActivity();
    }

    private void initActivity() {
        SearchView searchView = binding.searchView;
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.app_text));
        searchEditText.setHintTextColor(getResources().getColor(R.color.app_text));
        binding.searchView.onActionViewExpanded();
        binding.searchView.clearFocus();
        setTextListener();
        setOnBackPressedListener();
    }

    private void setOnBackPressedListener() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            activity.onBackPressed();
        });
    }

    public void setTextListener() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearchHandler(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                startSearchHandler(newText, 1000);
                return false;
            }
        });
    }

    private void startSearchHandler(String newText, long mills) {
        if (newText == null || newText.length() == 0) {
            setInitialState();
            setAdapter(new ArrayList<>());
            handlerQueryTextChange.removeCallbacks(runnableQueryTextChange);
            return;
        }
        setSearchingState();

        handlerQueryTextChange.removeCallbacks(runnableQueryTextChange);
        runnableQueryTextChange = () -> {
            performSearch(newText);
        };
        handlerQueryTextChange.postDelayed(runnableQueryTextChange, mills);
    }

    String searchTerm = "";
    private void performSearch(String result) {
        if (result != null && result.length() > 0) {
            setSearchingState();
            searchTerm = result;
            api.searchUsers(searchTerm).enqueue(new Callback<List<OutUserDTO>>() {
                @Override
                public void onResponse(Call<List<OutUserDTO>> call, Response<List<OutUserDTO>> response) {
                    if (searchTerm.equals(binding.searchView.getQuery().toString())) {
                        HelperMethods.hideKeyboard(activity);
                        if (response.isSuccessful()) {
                            setAdapter(response.body());
                            if (response.body() == null || response.body().isEmpty()) setNoneFound();
                            else setSearchDoneState();
                        } else {
                            setNoneFound();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<OutUserDTO>> call, Throwable throwable) {
                    if (searchTerm.equals(binding.searchView.getQuery().toString())) {
                        setNoneFound();
                    }
                }
            });
        } else {
            setAdapter(new ArrayList<>());
            setInitialState();
        }
    }

    void setSearchingState() {
        binding.constraintLayoutSearchNotStarted.setVisibility(View.GONE);
        binding.constraintLayoutNoneFound.setVisibility(View.GONE);
        binding.recyclerView.setAlpha(0.3f);
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.progressLoader.setVisibility(View.VISIBLE);
    }

    void setSearchDoneState() {
        binding.recyclerView.setAlpha(1f);
        binding.progressLoader.setVisibility(View.GONE);
        binding.constraintLayoutNoneFound.setVisibility(View.GONE);
    }

    void setInitialState() {
        binding.constraintLayoutSearchNotStarted.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.progressLoader.setVisibility(View.GONE);
        binding.constraintLayoutNoneFound.setVisibility(View.GONE);
    }

    void setNoneFound() {
        binding.progressLoader.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.constraintLayoutNoneFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAdapter(List<OutUserDTO> foundUsers) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(new RVAdapterFriendSearchItem(foundUsers, application.getCurrentUser(), activity, api, this));
    }
}