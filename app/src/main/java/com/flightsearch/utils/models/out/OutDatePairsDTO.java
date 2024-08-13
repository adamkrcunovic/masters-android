package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OutDatePairsDTO {

    @SerializedName("startDate")
    @Expose
    private String startDate;

    @SerializedName("endDate")
    @Expose
    private String endDate;

    @SerializedName("publicHolidays")
    @Expose
    private List<String> publicHolidays;

    @SerializedName("weekends")
    @Expose
    private List<String> weekends;

    @SerializedName("daysOff")
    @Expose
    private List<String> daysOff;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getPublicHolidays() {
        return publicHolidays;
    }

    public void setPublicHolidays(List<String> publicHolidays) {
        this.publicHolidays = publicHolidays;
    }

    public List<String> getWeekends() {
        return weekends;
    }

    public void setWeekends(List<String> weekends) {
        this.weekends = weekends;
    }

    public List<String> getDaysOff() {
        return daysOff;
    }

    public void setDaysOff(List<String> daysOff) {
        this.daysOff = daysOff;
    }
}
