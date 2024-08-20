package com.flightsearch.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.utils.models.helper.TripsDTO;
import com.flightsearch.utils.models.out.OutCountryDTO;
import com.flightsearch.utils.models.out.OutTripDTO;
import com.flightsearch.utils.models.out.OutUserDTO;
import com.flightsearch.utils.network.service.FlightSearchServicesApi;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltAndroidApp
public class MainApplication extends Application implements ApplicationConstants {

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    FlightSearchServicesApi api;

    private List<OutCountryDTO> allCountries;
    private OutUserDTO currentUser;
    private List<List<OutTripDTO>> myTrips;

    public boolean isUserLogged() {
        return sharedPreferences.getString(USER_AUTHORIZATION_TOKEN, null) != null;
    }

    public String userAuthorizationToken() {
        return sharedPreferences.getString(USER_AUTHORIZATION_TOKEN, null);
    }

    public void clearUserAuthorizationToken() {
        sharedPreferences.edit().remove(USER_AUTHORIZATION_TOKEN).apply();
    }

    public void clearUserData() {
        sharedPreferences.edit().remove(USER_DATA).apply();
    }

    public void clearTripsData() {
        sharedPreferences.edit().remove(TRIPS_DATA).apply();
    }

    public void storeUserAuthorizationToken(String token) {
        sharedPreferences.edit().putString(USER_AUTHORIZATION_TOKEN, token).apply();
    }

    public boolean isUserFirstTimeAppOpening() {
        return sharedPreferences.getBoolean(APP_FIRST_TIME_OPENING, true);
    }

    public void setIntroOpened() {
        sharedPreferences.edit().putBoolean(APP_FIRST_TIME_OPENING, false).apply();
    }

    public List<OutCountryDTO> getAllCountries() {
        return allCountries;
    }

    public List<List<OutTripDTO>> getMyTrips() {
        if (myTrips == null) myTrips = (new Gson()).fromJson(sharedPreferences.getString(TRIPS_DATA, null), TripsDTO.class).getTrips();
        return myTrips;
    }

    public void setMyTrips(List<List<OutTripDTO>> myTrips) {
        this.myTrips = myTrips;
    }

    public void getAllCountriesApiCall() {
        api.getCountries().enqueue(new Callback<List<OutCountryDTO>>() {
            @Override
            public void onResponse(Call<List<OutCountryDTO>> call, Response<List<OutCountryDTO>> response) {
                if (response.isSuccessful()) {
                    allCountries = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<OutCountryDTO>> call, Throwable throwable) {

            }
        });
    }

    public void getUser() {
        api.getUserData().enqueue(new Callback<OutUserDTO>() {
            @Override
            public void onResponse(Call<OutUserDTO> call, Response<OutUserDTO> response) {
                if (response.isSuccessful()) {
                    setCurrentUser(response.body());
                    getTrips();
                }
            }

            @Override
            public void onFailure(Call<OutUserDTO> call, Throwable throwable) {

            }
        });
    }

    private void getTrips() {
        api.getTrips().enqueue(new Callback<List<OutTripDTO>>() {
            @Override
            public void onResponse(Call<List<OutTripDTO>> call, Response<List<OutTripDTO>> response) {
                if (response.isSuccessful()) {
                    List<OutTripDTO> pastTrips = new ArrayList<>();
                    List<OutTripDTO> currentTrips = new ArrayList<>();
                    List<OutTripDTO> upcomingTrips = new ArrayList<>();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    for (OutTripDTO trip: response.body()
                         ) {
                        String departureDate = trip.getToSegments().get(0).getDeparture();
                        String arrivalDate = (trip.getFromSegments() != null && !trip.getFromSegments().isEmpty()) ? trip.getFromSegments().get(trip.getFromSegments().size() - 1).getArrival() : trip.getToSegments().get(trip.getToSegments().size() - 1).getArrival();
                        try {
                            Date departure = sdf.parse(departureDate);
                            Date arrival = sdf.parse(arrivalDate);
                            Date now = new Date();
                            if (now.after(departure) && now.after(arrival)) pastTrips.add(trip);
                            if (now.after(departure) && now.before(arrival)) currentTrips.add(trip);
                            if (now.before(departure) && now.before(arrival)) upcomingTrips.add(trip);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    myTrips = new ArrayList<>();
                    myTrips.add(pastTrips);
                    myTrips.add(currentTrips);
                    myTrips.add(upcomingTrips);
                    sharedPreferences.edit().putString(TRIPS_DATA, (new Gson()).toJson(new TripsDTO(myTrips))).apply();
                }
            }

            @Override
            public void onFailure(Call<List<OutTripDTO>> call, Throwable throwable) {

            }
        });
    }

    public OutUserDTO getCurrentUser() {
        if (currentUser == null) currentUser = (new Gson()).fromJson(sharedPreferences.getString(USER_DATA, null), OutUserDTO.class);
        return currentUser;
    }

    public void setCurrentUser(OutUserDTO currentUser) {
        this.currentUser = currentUser;
        sharedPreferences.edit().putString(USER_DATA, (new Gson()).toJson(currentUser)).apply();
    }

    public void signUserOut() {
        clearUserAuthorizationToken();
        clearUserData();
        clearTripsData();
    }
}
