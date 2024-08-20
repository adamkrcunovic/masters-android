package com.flightsearch.utils.models.helper;

import java.util.List;

public class PlanDayDTO {

    private String day;
    private List<PlanAttractionsDTO> planAttractions;

    public PlanDayDTO(String day, List<PlanAttractionsDTO> planAttractions) {
        this.day = day;
        this.planAttractions = planAttractions;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<PlanAttractionsDTO> getPlanAttractions() {
        return planAttractions;
    }

    public void setPlanAttractions(List<PlanAttractionsDTO> planAttractions) {
        this.planAttractions = planAttractions;
    }
}
