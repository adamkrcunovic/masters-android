package com.flightsearch.utils.network.service;

import com.flightsearch.utils.models.in.InRegisterDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FlightSearchServicesApi {

    @POST("api/account/register")
    Call<Void> register(@Body InRegisterDTO inRegisterDTO);

}
