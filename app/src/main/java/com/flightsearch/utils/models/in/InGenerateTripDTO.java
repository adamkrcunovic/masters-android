package com.flightsearch.utils.models.in;

import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.flightsearch.utils.models.out.OutFlightSegmentDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InGenerateTripDTO implements Serializable {

    @SerializedName("toSegments")
    @Expose
    private List<OutFlightSegmentDTO> toSegments;

    @SerializedName("layoverToDuration")
    @Expose
    private List<String> layoverToDuration;

    @SerializedName("fromSegments")
    @Expose
    private List<OutFlightSegmentDTO> fromSegments;

    @SerializedName("layoverFromDuration")
    @Expose
    private List<String> layoverFromDuration;

    public InGenerateTripDTO(OutFlightDealDTO flightDeal) {
        toSegments = flightDeal.getToSegments();
        layoverToDuration = flightDeal.getLayoverToDuration();
        fromSegments = flightDeal.getFromSegments();
        layoverFromDuration = flightDeal.getLayoverFromDuration();
    }

    public List<OutFlightSegmentDTO> getToSegments() {
        return toSegments;
    }

    public void setToSegments(List<OutFlightSegmentDTO> toSegments) {
        this.toSegments = toSegments;
    }

    public List<String> getLayoverToDuration() {
        return layoverToDuration;
    }

    public void setLayoverToDuration(List<String> layoverToDuration) {
        this.layoverToDuration = layoverToDuration;
    }

    public List<OutFlightSegmentDTO> getFromSegments() {
        return fromSegments;
    }

    public void setFromSegments(List<OutFlightSegmentDTO> fromSegments) {
        this.fromSegments = fromSegments;
    }

    public List<String> getLayoverFromDuration() {
        return layoverFromDuration;
    }

    public void setLayoverFromDuration(List<String> layoverFromDuration) {
        this.layoverFromDuration = layoverFromDuration;
    }
}
