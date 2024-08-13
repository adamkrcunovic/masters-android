package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OutFlightDTO {

    @SerializedName("cheapestFlights")
    @Expose
    private List<OutFlightDealDTO> cheapestFlights;

    @SerializedName("fastestFlights")
    @Expose
    private List<OutFlightDealDTO> fastestFlights;

    @SerializedName("longestStayFlights")
    @Expose
    private List<OutFlightDealDTO> longestStayFlights;

    @SerializedName("cityVisit")
    @Expose
    private List<OutFlightDealDTO> cityVisit;

    public List<OutFlightDealDTO> getCheapestFlights() {
        return cheapestFlights;
    }

    public void setCheapestFlights(List<OutFlightDealDTO> cheapestFlights) {
        this.cheapestFlights = cheapestFlights;
    }

    public List<OutFlightDealDTO> getFastestFlights() {
        return fastestFlights;
    }

    public void setFastestFlights(List<OutFlightDealDTO> fastestFlights) {
        this.fastestFlights = fastestFlights;
    }

    public List<OutFlightDealDTO> getLongestStayFlights() {
        return longestStayFlights;
    }

    public void setLongestStayFlights(List<OutFlightDealDTO> longestStayFlights) {
        this.longestStayFlights = longestStayFlights;
    }

    public List<OutFlightDealDTO> getCityVisit() {
        return cityVisit;
    }

    public void setCityVisit(List<OutFlightDealDTO> cityVisit) {
        this.cityVisit = cityVisit;
    }
}
