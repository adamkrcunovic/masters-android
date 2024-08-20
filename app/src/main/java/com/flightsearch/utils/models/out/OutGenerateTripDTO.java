package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutGenerateTripDTO {

    @SerializedName("generatedTrip")
    @Expose
    private String generatedTrip;

    public String getGeneratedTrip() {
        return generatedTrip;
    }

    public void setGeneratedTrip(String generatedTrip) {
        this.generatedTrip = generatedTrip;
    }

}
