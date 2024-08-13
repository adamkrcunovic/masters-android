package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutFlightSegmentDTO {

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("departure")
    @Expose
    private String departure;

    @SerializedName("arrival")
    @Expose
    private String arrival;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("flightCode")
    @Expose
    private String flightCode;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }
}
