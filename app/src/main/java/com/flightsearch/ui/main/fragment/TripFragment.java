package com.flightsearch.ui.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.databinding.FragmentTripBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.adapter.RVAdapterComment;
import com.flightsearch.ui.main.adapter.RVAdapterFlightDealItinerary;
import com.flightsearch.utils.firebase.BearerTokenGoogle;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.out.OutFlightDTO;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutFlightSegmentDTO;
import com.flightsearch.utils.models.out.OutTripDTO;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class TripFragment extends Fragment {

    @Inject
    MainApplication application;

    @Inject
    FlightSearchServicesApi api;

    private FragmentTripBinding binding;
    private OutFlightDealDTO flightDeal;
    private OutTripDTO flightTrip;

    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        binding = FragmentTripBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTrip();
        setLayout();
    }

    private void getTrip() {
        flightDeal = (OutFlightDealDTO) TripFragmentArgs.fromBundle(getArguments()).getFlightDeal();
        if (flightDeal instanceof OutTripDTO) flightTrip = (OutTripDTO) flightDeal;
    }

    private void setLayout() {
        boolean isTrip = flightTrip != null;

        if (isTrip) {
            boolean amICreator = flightTrip.creator.getId().equals(application.getCurrentUser().getId());

            binding.textViewCreatorTitle.setVisibility(View.VISIBLE);
            binding.textViewCreator.setVisibility(View.VISIBLE);
            binding.textViewCreator.setText(flightTrip.getCreator().getName() + " " + flightTrip.getCreator().getLastName());
            binding.textViewMembersTitle.setVisibility(View.VISIBLE);

            String members = "";
            for (OutUserDTO member : flightTrip.getInvitedMembers()
            ) {
                members += member.getName() + " " + member.getLastName() + (flightTrip.getInvitedMembers().indexOf(member) != flightTrip.getInvitedMembers().size() - 1 ? "\n" : "");
            }
            binding.textViewMembers.setVisibility(View.VISIBLE);
            if (members.length() > 0) binding.textViewMembers.setText(members);
            else binding.textViewMembers.setText("There are no members");
            binding.materialButtonAddMember.setVisibility(amICreator ? View.VISIBLE : View.GONE);

            if (flightTrip.getComments() != null && !flightTrip.getComments().isEmpty()) {
                binding.textViewComments.setVisibility(View.VISIBLE);
                binding.recyclerViewComments.setVisibility(View.VISIBLE);
                binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                binding.recyclerViewComments.setAdapter(new RVAdapterComment(flightTrip.getComments()));
            }

            binding.view1.setVisibility(View.VISIBLE);
            binding.view2.setVisibility(View.VISIBLE);
            binding.constraintLayoutComment.setVisibility(View.VISIBLE);
            binding.textViewCommenterName.setText(application.getCurrentUser().getName() + " " + application.getCurrentUser().getLastName());

            binding.materialButtonPost.setOnClickListener(v -> {
                String postText = binding.textInputEditTextComment.getText().toString();
                if (!postText.isEmpty()) {
                    activity.showDialog();
                    api.AddComment(flightTrip.getId(), postText).enqueue(new Callback<List<String>>() {
                        @Override
                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                            activity.dismissDialog();
                            if (response.isSuccessful()) {
                                BearerTokenGoogle.sendNotifications(response.body(), "New comment", application.getCurrentUser().getName() + " " + application.getCurrentUser().getLastName() + " has added comment on " + flightTrip.getItineraryName());
                            }
                        }

                        @Override
                        public void onFailure(Call<List<String>> call, Throwable throwable) {
                            activity.dismissDialog();
                        }
                    });
                }
            });
        }

        binding.materialToolbar.setTitle(isTrip ? flightTrip.getItineraryName() : "Flight Deal");

        binding.textViewFlight1DepartureDate.setText("Departure - " + HelperMethods.dateStringToDateWithName(flightDeal.getToSegments().get(0).getDeparture()));
        binding.textViewFlight1ArrivalDate.setText("Arrive - " + HelperMethods.dateStringToDateWithName(flightDeal.getToSegments().get(flightDeal.getToSegments().size() - 1).getArrival()));

        binding.recyclerViewFlight1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        RVAdapterFlightDealItinerary adapter1 = new RVAdapterFlightDealItinerary(flightDeal.getToSegments(), flightDeal.getLayoverToDuration());
        binding.recyclerViewFlight1.setAdapter(adapter1);

        if (flightDeal.getFromSegments() != null && !flightDeal.getFromSegments().isEmpty()) {

            binding.materialCardViewFlight2.setVisibility(View.VISIBLE);
            binding.textViewFlight2DepartureDate.setVisibility(View.VISIBLE);
            binding.textViewFlight2ArrivalDate.setVisibility(View.VISIBLE);

            binding.textViewFlight2DepartureDate.setText("Departure - " + HelperMethods.dateStringToDateWithName(flightDeal.getFromSegments().get(0).getDeparture()));
            binding.textViewFlight2ArrivalDate.setText("Arrive - " + HelperMethods.dateStringToDateWithName(flightDeal.getFromSegments().get(flightDeal.getFromSegments().size() - 1).getArrival()));

            binding.recyclerViewFlight2.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            RVAdapterFlightDealItinerary adapter2 = new RVAdapterFlightDealItinerary(flightDeal.getFromSegments(), flightDeal.getLayoverFromDuration());
            binding.recyclerViewFlight2.setAdapter(adapter2);
        }
    }
}