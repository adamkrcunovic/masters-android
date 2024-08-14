package com.flightsearch.utils.network.service;

import com.flightsearch.utils.models.in.InFlightSearchDTO;
import com.flightsearch.utils.models.in.InLoginDTO;
import com.flightsearch.utils.models.in.InRegisterDTO;
import com.flightsearch.utils.models.out.OutCountryDTO;
import com.flightsearch.utils.models.out.OutDatePairsDTO;
import com.flightsearch.utils.models.out.OutFlightDTO;
import com.flightsearch.utils.models.out.OutUserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FlightSearchServicesApi {

    @POST("api/account/register")
    Call<String> register(@Body InRegisterDTO inRegisterDTO);

    @POST("api/account/login")
    Call<String> signIn(@Body InLoginDTO inLoginDTO);

    @PUT("api/account/logout/{deviceId}")
    Call<Void> signOut(@Path("deviceId") String deviceId);

    @GET("api/account/getUserData")
    Call<OutUserDTO> getUserData();

    @GET("api/country")
    Call<List<OutCountryDTO>> getCountries();

    @GET("api/flights/getTravelPairs")
    Call<List<OutDatePairsDTO>> getDatePairs(@Query("TotalDays") int totalDays, @Query("DaysOff") int DaysOff);

    @PATCH("api/flights/search")
    Call<OutFlightDTO> getFlightDeals(@Body InFlightSearchDTO inFlightSearchDTO);

    @GET("api/friend/search/{searchTerm}")
    Call<List<OutUserDTO>> searchUsers(@Path("searchTerm") String searchTerm);

}
