package com.flightsearch.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flightsearch.R;
import com.flightsearch.application.MainApplication;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.databinding.FragmentTripBinding;
import com.flightsearch.ui.main.activity.MainActivity;
import com.flightsearch.ui.main.adapter.RVAdapterAttraction;
import com.flightsearch.ui.main.adapter.RVAdapterComment;
import com.flightsearch.ui.main.adapter.RVAdapterFlightDealItinerary;
import com.flightsearch.ui.userEntry.activity.UserEntryActivity;
import com.flightsearch.utils.base.BaseFragment;
import com.flightsearch.utils.base.bottomSheet.BottomSheetChooseRadio;
import com.flightsearch.utils.base.bottomSheet.BottomSheetInfo;
import com.flightsearch.utils.firebase.BearerTokenGoogle;
import com.flightsearch.utils.helpers.HelperMethods;
import com.flightsearch.utils.models.enums.PriceChangeNotificationType;
import com.flightsearch.utils.models.helper.PlanAttractionsDTO;
import com.flightsearch.utils.models.helper.PlanDayDTO;
import com.flightsearch.utils.models.in.InGenerateTripDTO;
import com.flightsearch.utils.models.in.InTripDTO;
import com.flightsearch.utils.models.out.OutCommentDTO;
import com.flightsearch.utils.models.out.OutFlightDTO;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutFlightSegmentDTO;
import com.flightsearch.utils.models.out.OutGenerateTripDTO;
import com.flightsearch.utils.models.out.OutTripDTO;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;
import com.google.android.material.tabs.TabLayout;
import com.google.api.client.json.Json;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class TripFragment extends BaseFragment implements ApplicationConstants {

    @Inject
    MainApplication application;

    @Inject
    FlightSearchServicesApi api;

    private FragmentTripBinding binding;
    private OutFlightDealDTO flightDeal;
    private int adults;
    private OutTripDTO flightTrip;
    private List<PlanDayDTO> dayPlans;

    private MainActivity activity;

    private String setNotificationTypeSelection = "Not Receiving";
    private List<String> notificationTypeSelection = new ArrayList<String>() {{add("Not Receiving");add("Receiving - Percentage");add("Receiving - Amount");}};
    private String ChatGPTText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        binding = FragmentTripBinding.bind(view);
        activity = (MainActivity) getActivity();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flightTrip != null && activity.usersToAddToItinerary.size() > 0) {
            flightTrip.getInvitedMembers().addAll(activity.usersToAddToItinerary);
            activity.usersToAddToItinerary = new ArrayList<>();
        }
        setLayout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTrip();
        setLayout();
        setOnClickListeners();
        setOnFocusChangeListeners();
        setOnBackPressedListener();
    }

    private void getTrip() {
        flightDeal = (OutFlightDealDTO) TripFragmentArgs.fromBundle(getArguments()).getFlightDeal();
        if (flightDeal instanceof OutTripDTO) flightTrip = (OutTripDTO) flightDeal;
        adults = TripFragmentArgs.fromBundle(getArguments()).getAdults();
    }

    private void setLayout() {
        boolean isTrip = flightTrip != null;
        boolean amICreator = application.isUserLogged() && isTrip && flightTrip.creator.getId().equals(application.getCurrentUser().getId());

        if (isTrip) {
            binding.textViewOldPriceValue.setText(Math.round(flightTrip.getTotalPrice()) + "€");
            binding.textInputLayoutItineraryName.setVisibility(View.GONE);
            binding.textViewTripNameTitle.setVisibility(View.GONE);

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

            binding.textViewComments.setVisibility(View.VISIBLE);
            if (flightTrip.getComments() != null && !flightTrip.getComments().isEmpty()) {
                binding.recyclerViewComments.setVisibility(View.VISIBLE);
                binding.recyclerViewComments.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                binding.recyclerViewComments.setAdapter(new RVAdapterComment(flightTrip.getComments()));
            }

            binding.view1.setVisibility(View.VISIBLE);
            binding.view2.setVisibility(View.VISIBLE);
            binding.constraintLayoutComment.setVisibility(View.VISIBLE);
            binding.textViewCommenterName.setText(application.getCurrentUser().getName() + " " + application.getCurrentUser().getLastName());
            binding.textViewCurrentPriceValue.setText(Math.round(flightTrip.getCurrentPrice()) + "€");
        } else {
            binding.textViewOldPriceTitle.setVisibility(View.GONE);
            binding.textViewOldPriceValue.setVisibility(View.GONE);
            binding.textViewCurrentPriceValue.setText(Math.round(flightDeal.getTotalPrice()) + "€");
        }

        binding.materialToolbar.setTitle(isTrip ? flightTrip.getItineraryName() : "Flight Deal");

        binding.textViewFlight1DepartureDate.setText("Departure - " + HelperMethods.dateStringToDateWithName(flightDeal.getToSegments().get(0).getDeparture()));
        binding.textViewFlight1ArrivalDate.setText("Arrive - " + HelperMethods.dateStringToDateWithName(flightDeal.getToSegments().get(flightDeal.getToSegments().size() - 1).getArrival()));

        binding.recyclerViewFlight1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        RVAdapterFlightDealItinerary adapter1 = new RVAdapterFlightDealItinerary(flightDeal.getToSegments(), flightDeal.getLayoverToDuration());
        binding.recyclerViewFlight1.setAdapter(adapter1);

        binding.materialButtonSignIn.setVisibility(!application.isUserLogged() ? View.VISIBLE : View.GONE);
        binding.materialButtonRegister.setVisibility(!application.isUserLogged() ? View.VISIBLE : View.GONE);
        binding.textViewSignInRegisterText.setVisibility(!application.isUserLogged() ? View.VISIBLE : View.GONE);

        if (flightDeal.getFromSegments() != null && !flightDeal.getFromSegments().isEmpty()) {

            if (isTrip) {
                ChatGPTText = flightTrip.getChatGPTGeneratedText();
                if (flightTrip.getChatGPTGeneratedText() == null || flightTrip.getChatGPTGeneratedText().length() == 0) {
                    binding.materialButtonGenerateTrip.setVisibility(View.VISIBLE);
                } else {
                    try {
                        dayPlans = HelperMethods.parseGeneratedText(flightTrip.getChatGPTGeneratedText());
                        setGeneratedPlan();
                    } catch (Exception e) {

                    }
                }
            } else {
                binding.materialButtonGenerateTrip.setVisibility(application.isUserLogged() ? View.VISIBLE : View.GONE);
            }

            binding.constraintLayoutFlight2.setVisibility(View.VISIBLE);
            binding.materialCardViewFlight2.setVisibility(View.VISIBLE);
            binding.textViewFlight2DepartureDate.setVisibility(View.VISIBLE);
            binding.textViewFlight2ArrivalDate.setVisibility(View.VISIBLE);

            binding.textViewFlight2DepartureDate.setText("Departure - " + HelperMethods.dateStringToDateWithName(flightDeal.getFromSegments().get(0).getDeparture()));
            binding.textViewFlight2ArrivalDate.setText("Arrive - " + HelperMethods.dateStringToDateWithName(flightDeal.getFromSegments().get(flightDeal.getFromSegments().size() - 1).getArrival()));

            binding.recyclerViewFlight2.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            RVAdapterFlightDealItinerary adapter2 = new RVAdapterFlightDealItinerary(flightDeal.getFromSegments(), flightDeal.getLayoverFromDuration());
            binding.recyclerViewFlight2.setAdapter(adapter2);
        }
        binding.constraintLayoutAddTrip.setVisibility(!isTrip || amICreator ? View.VISIBLE : View.GONE);
        binding.materialButtonSaveTrip.setVisibility(!isTrip || amICreator ? View.VISIBLE : View.GONE);
    }

    private void setOnClickListeners() {
        binding.materialButtonPost.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(activity);
            setCommentErrors();
            if (binding.textInputLayoutComment.getError() == null) {
                String postText = binding.textInputEditTextComment.getText().toString();
                activity.showDialog();
                api.addComment(flightTrip.getId(), postText).enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        activity.dismissDialog();
                        if (response.isSuccessful()) {
                            binding.textInputEditTextComment.setText("");
                            BearerTokenGoogle.sendNotifications(response.body(), "New comment", application.getCurrentUser().getName() + " " + application.getCurrentUser().getLastName() + " has added comment on " + flightTrip.getItineraryName());
                            flightTrip.getComments().add(new OutCommentDTO(postText, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()), application.getCurrentUser()));
                            binding.recyclerViewComments.getAdapter().notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable throwable) {
                        activity.dismissDialog();
                    }
                });
            }
        });
        binding.materialButtonAddMember.setOnClickListener(v -> {
            ArrayList<String> members = new ArrayList<>();
            members.add(flightTrip.getCreator().getId());
            for (OutUserDTO member: flightTrip.getInvitedMembers()
                 ) {
                members.add(member.getId());
            }
            activity.getNavController().navigate(TripFragmentDirections.actionTripFragmentToSearchFriendsFragment().setFromTripFragment(true).setInvitedMembers(members).setItineraryId(flightTrip.getId()));
        });
        binding.materialButtonGenerateTrip.setOnClickListener(v -> {
            callGenerateTrip();
        });
        binding.materialButtonSignIn.setOnClickListener(v -> {
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_BACK.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, true));
        });
        binding.materialButtonRegister.setOnClickListener(v -> {
            startActivity((new Intent(getActivity(), UserEntryActivity.class)).putExtra(NEXT_USER_ENTRY_PAGE, UserEntryActivity.NavigationMode.GO_BACK.getValue()).putExtra(INVERSE_USER_ENTRY_PAGES, false));
        });
        binding.materialButtonEditNotificationType.setOnClickListener(v -> {
            bottomSheetChooseRadioNotificationType = new BottomSheetChooseRadio(Arrays.asList(notificationTypeSelection), activity, new BottomSheetChooseRadio.OnObjectSelectListener() {
                @Override
                public void onObjectSelectChangeListener(Object o) {
                    String selectedType = (String) o;
                    if (selectedType.equals("Not Receiving")) {
                        binding.textInputLayoutNumber.setVisibility(View.GONE);
                    }
                    if (selectedType.equals("Receiving - Percentage")) {
                        binding.textInputLayoutNumber.setVisibility(View.VISIBLE);
                        binding.textInputEditTextNumber.setHint(activity.getString(R.string.hint_percentage));
                    }
                    if (selectedType.equals("Receiving - Amount")) {
                        binding.textInputLayoutNumber.setVisibility(View.VISIBLE);
                        binding.textInputEditTextNumber.setHint(activity.getString(R.string.hint_amount));
                    }
                    binding.textViewSaveTypeValue.setText(selectedType);
                    setNotificationTypeSelection = selectedType;
                    bottomSheetChooseRadioNotificationType.dismiss();
                }
            }, "Choose notification receiving type", setNotificationTypeSelection);
            bottomSheetChooseRadioNotificationType.show(activity);
        });
        binding.imageViewInfo.setOnClickListener(v -> {
            BottomSheetInfo bottomSheetInfo = new BottomSheetInfo("Amount and percentage notifications", "You will be notified when price goes up or drops down for your defined amount in percentages or euros.");
            bottomSheetInfo.show(activity);
        });
        binding.materialButtonChangePlan.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(activity);
            String changePlanText = binding.textInputEditTextChangePlan.getText().toString();
            if (changePlanText.isEmpty()) binding.textInputLayoutChangePlan.setError(activity.getString(R.string.error_field_required));
            else {
                //do change plan!!!
            }
        });
        binding.materialButtonSaveTrip.setOnClickListener(v -> {
            HelperMethods.hideKeyboard(activity);
            String itineraryName = binding.textInputEditTextItineraryName.getText().toString();
            if (itineraryName.isEmpty()) binding.textInputLayoutItineraryName.setError(activity.getString(R.string.error_field_required));
            else {
                if (binding.textInputLayoutNumber.getVisibility() == View.VISIBLE && binding.textInputEditTextNumber.getText().toString().isEmpty()) binding.textInputLayoutNumber.setError(activity.getString(R.string.error_field_required));
                else {
                    int value = Integer.valueOf(binding.textInputEditTextNumber.getText().toString());
                    saveItinerary(itineraryName, value);
                }
            }
        });
    }
    BottomSheetChooseRadio bottomSheetChooseRadioNotificationType = null;

    private void callGenerateTrip() {
        activity.showDialog();
        api.generateTrip(new InGenerateTripDTO(flightDeal)).enqueue(new Callback<OutGenerateTripDTO>() {
            @Override
            public void onResponse(Call<OutGenerateTripDTO> call, Response<OutGenerateTripDTO> response) {
                activity.dismissDialog();
                if (response.isSuccessful()) {
                    List<PlanDayDTO> parsedGeneratedTripData = HelperMethods.parseGeneratedText(response.body().getGeneratedTrip());
                    dayPlans = parsedGeneratedTripData;
                    ChatGPTText = response.body().getGeneratedTrip();
                    setGeneratedPlan();
                }
            }

            @Override
            public void onFailure(Call<OutGenerateTripDTO> call, Throwable throwable) {
                activity.dismissDialog();
            }
        });
    }

    private void setGeneratedPlan() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int index = tab.getPosition();
                setChatGPTText(index);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        for (PlanDayDTO dayPlan: dayPlans
        ) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(dayPlan.getDay()));
            /*for (PlanAttractionsDTO attraction: dayPlan.getPlanAttractions()
                 ) {
                System.out.println("Attraction: " + attraction.getAttractionName());
                System.out.println("AttractionLatitude: " + attraction.getAttractionLatitude());
                System.out.println("AttractionLongitude: " + attraction.getAttractionLongitude());
                System.out.println("AttractionPlan: " + attraction.getGetAttractionText());
            }*/
        }
        setChatGPTText(0);
        binding.materialButtonGenerateTrip.setVisibility(View.GONE);
        binding.constraintLayoutGeneratedTrip.setVisibility(View.VISIBLE);
        boolean isTrip = flightTrip != null;
        boolean amICreator = application.isUserLogged() && isTrip && flightTrip.creator.getId().equals(application.getCurrentUser().getId());
        binding.constraintLayoutChangePlan.setVisibility(!isTrip || amICreator ? View.VISIBLE : View.GONE);
    }

    private void setChatGPTText(int i) {
        binding.recyclerViewGeneratedPlan.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        binding.recyclerViewGeneratedPlan.setAdapter(new RVAdapterAttraction(dayPlans.get(i).getPlanAttractions()));
        Glide.with(activity).load(HelperMethods.getGoogleImageUrlItinerary(dayPlans.get(i).getPlanAttractions())).fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imageViewItinerary);
    }

    private void setOnFocusChangeListeners() {
        binding.textInputEditTextComment.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutComment.setError(null);
            }
        });
        binding.textInputEditTextItineraryName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutItineraryName.setError(null);
            }
        });
        binding.textInputEditTextNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.textInputLayoutNumber.setError(null);
            }
        });
    }

    private void setCommentErrors() {
        if (TextUtils.isEmpty(binding.textInputEditTextComment.getText())) binding.textInputLayoutComment.setError(activity.getString(R.string.error_field_required));
    }

    private void setOnBackPressedListener() {
        binding.materialToolbar.setNavigationOnClickListener(v -> {
            activity.onBackPressed();
        });
    }

    private void saveItinerary(String itineraryName, int value) {
        PriceChangeNotificationType priceChangeNotificationType = PriceChangeNotificationType.NotSet;
        int amount = 0;
        int percentage = 0;
        if (setNotificationTypeSelection.equals("Not Receiving")) {
            priceChangeNotificationType = PriceChangeNotificationType.NotSet;
        }
        if (setNotificationTypeSelection.equals("Receiving - Percentage")) {
            priceChangeNotificationType = PriceChangeNotificationType.Percentage;
            percentage = value;
        }
        if (setNotificationTypeSelection.equals("Receiving - Amount")) {
            priceChangeNotificationType = PriceChangeNotificationType.Amount;
            amount = value;
        }
        InTripDTO inTripDTO = new InTripDTO(itineraryName, adults, flightDeal, ChatGPTText, priceChangeNotificationType, percentage, amount);
        activity.showDialog();
        api.saveTrip(inTripDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                activity.dismissDialog();
                if (response.isSuccessful()) {
                    activity.onBackPressed();
                    activity.onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                activity.dismissDialog();
            }
        });
    }
}