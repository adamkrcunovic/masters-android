package com.flightsearch.utils.models.helper;

import com.flightsearch.utils.models.out.OutTripDTO;

import java.util.List;

public class TripsDTO {

    private List<List<OutTripDTO>> trips;

    public TripsDTO(List<List<OutTripDTO>> trips) {
        this.trips = trips;
    }

    public List<List<OutTripDTO>> getTrips() {
        return trips;
    }

    public void setTrips(List<List<OutTripDTO>> trips) {
        this.trips = trips;
    }
}
