package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OutCountryDTO {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("countryName")
    @Expose
    private String countryName;

    @SerializedName("publicHolidays")
    @Expose
    private List<String> publicHolidays;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<String> getPublicHolidays() {
        return publicHolidays;
    }

    public void setPublicHolidays(List<String> publicHolidays) {
        this.publicHolidays = publicHolidays;
    }
}
