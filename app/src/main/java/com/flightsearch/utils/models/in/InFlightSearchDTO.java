package com.flightsearch.utils.models.in;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InFlightSearchDTO {

    @SerializedName("FlightSearchType")
    @Expose
    private int flightSearchType;

    @SerializedName("DepartureDay")
    @Expose
    private String departureDay;

    @SerializedName("ReturnDay")
    @Expose
    private String returnDay;

    @SerializedName("Adults")
    @Expose
    private int adults;

    @SerializedName("Month")
    @Expose
    private int month;

    @SerializedName("Year")
    @Expose
    private int year;

    @SerializedName("TripDuration")
    @Expose
    private int tripDuration;

    @SerializedName("FromAirport")
    @Expose
    private String fromAirport;

    @SerializedName("MultiCity1")
    @Expose
    private String multiCity1;

    @SerializedName("MultiCity2")
    @Expose
    private String multiCity2;

    @SerializedName("ToAirport")
    @Expose
    private String toAirport;

    @SerializedName("MixMultiCity")
    @Expose
    private Boolean mixMultiCity = false;

    @SerializedName("FlyTheNightBefore")
    @Expose
    private Boolean flyTheNightBefore = false;

    @SerializedName("IncludedAirlineCodes")
    @Expose
    private String includedAirlineCodes;

    public int getFlightSearchType() {
        return flightSearchType;
    }

    public void setFlightSearchType(int flightSearchType) {
        this.flightSearchType = flightSearchType;
    }

    public String getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(String departureDay) {
        this.departureDay = departureDay;
    }

    public String getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(String returnDay) {
        this.returnDay = returnDay;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public String getFromAirport() {
        return fromAirport;
    }

    public void setFromAirport(String fromAirport) {
        this.fromAirport = fromAirport;
    }

    public String getMultiCity1() {
        return multiCity1;
    }

    public void setMultiCity1(String multiCity1) {
        this.multiCity1 = multiCity1;
    }

    public String getMultiCity2() {
        return multiCity2;
    }

    public void setMultiCity2(String multiCity2) {
        this.multiCity2 = multiCity2;
    }

    public String getToAirport() {
        return toAirport;
    }

    public void setToAirport(String toAirport) {
        this.toAirport = toAirport;
    }

    public Boolean getMixMultiCity() {
        return mixMultiCity;
    }

    public void setMixMultiCity(Boolean mixMultiCity) {
        this.mixMultiCity = mixMultiCity;
    }

    public Boolean getFlyTheNightBefore() {
        return flyTheNightBefore;
    }

    public void setFlyTheNightBefore(Boolean flyTheNightBefore) {
        this.flyTheNightBefore = flyTheNightBefore;
    }

    public String getIncludedAirlineCodes() {
        return includedAirlineCodes;
    }

    public void setIncludedAirlineCodes(String includedAirlineCodes) {
        this.includedAirlineCodes = includedAirlineCodes;
    }
}
