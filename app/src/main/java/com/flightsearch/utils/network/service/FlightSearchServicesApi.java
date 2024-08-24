package com.flightsearch.utils.network.service;

import com.flightsearch.utils.models.in.InEditProfileDTO;
import com.flightsearch.utils.models.in.InFlightSearchDTO;
import com.flightsearch.utils.models.in.InGenerateTripDTO;
import com.flightsearch.utils.models.in.InLoginDTO;
import com.flightsearch.utils.models.in.InRegisterDTO;
import com.flightsearch.utils.models.out.OutAirportDTO;
import com.flightsearch.utils.models.out.OutCountryDTO;
import com.flightsearch.utils.models.out.OutDatePairsDTO;
import com.flightsearch.utils.models.out.OutFlightDTO;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutGenerateTripDTO;
import com.flightsearch.utils.models.out.OutTripDTO;
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

    //region account
    @POST("api/account/register")
    Call<String> register(@Body InRegisterDTO inRegisterDTO);

    @POST("api/account/login")
    Call<String> signIn(@Body InLoginDTO inLoginDTO);

    @PUT("api/account/logout/{deviceId}")
    Call<Void> signOut(@Path("deviceId") String deviceId);

    @GET("api/account/getUserData")
    Call<OutUserDTO> getUserData();

    @PUT("api/account/editPersonalData")
    Call<Void> editProfile(@Body InEditProfileDTO inEditProfileDTO);
    //endregion

    //region flight tickets
    @GET("api/country")
    Call<List<OutCountryDTO>> getCountries();

    @GET("api/flights/getAirports/{searchTerm}")
    Call<List<OutAirportDTO>> getAirports(@Path("searchTerm") String searchTerm);

    @GET("api/flights/getTravelPairs")
    Call<List<OutDatePairsDTO>> getDatePairs(@Query("TotalDays") int totalDays, @Query("DaysOff") int DaysOff);

    @PATCH("api/flights/search")
    Call<OutFlightDTO> getFlightDeals(@Body InFlightSearchDTO inFlightSearchDTO);
    //endregion

    //region friendship
    @GET("api/friend/search/{searchTerm}")
    Call<List<OutUserDTO>> searchUsers(@Path("searchTerm") String searchTerm);

    @POST("api/friend/sendRequest/{friendId}")
    Call<List<String>> sendFriendRequest(@Path("friendId") String friendId);

    @PUT("api/friend/updateRequest/{friendId}/{acceptAndRejectRequest}")
    Call<List<String>> acceptOrRejectFriendRequest(@Path("friendId") String friendId, @Path("acceptAndRejectRequest") boolean acceptAndRejectRequest);
    //endregion

    //region trip
    @GET("api/trip/getTrips")
    Call<List<OutTripDTO>> getTrips();

    @POST("api/trip/inviteUserToTrip/{itineraryId}/{user}")
    Call<List<String>> inviteUserToTrip(@Path("itineraryId") int itineraryId, @Path("user") String user);

    @POST("api/trip/generate")
    Call<OutGenerateTripDTO> generateTrip(@Body InGenerateTripDTO inGenerateTripDTO);

    //subregion comments
    @POST("api/trip/addComment/{itineraryId}/{comment}")
    Call<List<String>> addComment(@Path("itineraryId") int itineraryId, @Path("comment") String comment);
    //endregion
}
