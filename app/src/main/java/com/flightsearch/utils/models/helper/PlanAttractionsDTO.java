package com.flightsearch.utils.models.helper;

public class PlanAttractionsDTO {

    private String attractionName;
    private Double attractionLatitude;
    private Double attractionLongitude;
    private String getAttractionText;

    public PlanAttractionsDTO(String attractionName, Double attractionLatitude, Double attractionLongitude, String getAttractionText) {
        this.attractionName = attractionName;
        this.attractionLatitude = attractionLatitude;
        this.attractionLongitude = attractionLongitude;
        this.getAttractionText = getAttractionText;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public Double getAttractionLatitude() {
        return attractionLatitude;
    }

    public void setAttractionLatitude(Double attractionLatitude) {
        this.attractionLatitude = attractionLatitude;
    }

    public Double getAttractionLongitude() {
        return attractionLongitude;
    }

    public void setAttractionLongitude(Double attractionLongitude) {
        this.attractionLongitude = attractionLongitude;
    }

    public String getGetAttractionText() {
        return getAttractionText;
    }

    public void setGetAttractionText(String getAttractionText) {
        this.getAttractionText = getAttractionText;
    }
}
