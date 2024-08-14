package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OutFlightDealDTO implements Serializable {

    @SerializedName("totalStayDuration")
    @Expose
    private String totalStayDuration;

    @SerializedName("toDuration")
    @Expose
    private String toDuration;

    @SerializedName("toSegments")
    @Expose
    private List<OutFlightSegmentDTO> toSegments;

    @SerializedName("layoverToDuration")
    @Expose
    private List<String> layoverToDuration;

    @SerializedName("cityVisit")
    @Expose
    private List<String> cityVisit;

    @SerializedName("fromDuration")
    @Expose
    private String fromDuration;

    @SerializedName("fromSegments")
    @Expose
    private List<OutFlightSegmentDTO> fromSegments;

    @SerializedName("layoverFromDuration")
    @Expose
    private List<String> layoverFromDuration;

    @SerializedName("totalPrice")
    @Expose
    private Double totalPrice;

    public String getTotalStayDuration() {
        return totalStayDuration;
    }

    public void setTotalStayDuration(String totalStayDuration) {
        this.totalStayDuration = totalStayDuration;
    }

    public String getToDuration() {
        return toDuration;
    }

    public void setToDuration(String toDuration) {
        this.toDuration = toDuration;
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

    public List<String> getCityVisit() {
        return cityVisit;
    }

    public void setCityVisit(List<String> cityVisit) {
        this.cityVisit = cityVisit;
    }

    public String getFromDuration() {
        return fromDuration;
    }

    public void setFromDuration(String fromDuration) {
        this.fromDuration = fromDuration;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
