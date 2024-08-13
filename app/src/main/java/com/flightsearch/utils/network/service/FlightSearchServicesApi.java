package com.flightsearch.utils.network.service;

import com.flightsearch.utils.models.in.InRegisterDTO;
import com.flightsearch.utils.models.out.OutCountryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FlightSearchServicesApi {

    @POST("api/account/register")
    Call<String> register(@Body InRegisterDTO inRegisterDTO);

    @GET("api/country")
    Call<List<OutCountryDTO>> getCountries();

}
