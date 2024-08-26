package com.flightsearch.utils.models.in;

import com.flightsearch.utils.models.enums.PriceChangeNotificationType;
import com.flightsearch.utils.models.out.OutFlightDealDTO;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InTripDTO {

    @SerializedName("ItineraryName")
    @Expose
    private String itineraryName;

    @SerializedName("Adults")
    @Expose
    private int adults;

    @SerializedName("OutFlightDealDTO")
    @Expose
    private OutFlightDealDTO outFlightDealDTO;

    @SerializedName("ChatGPTGeneratedText")
    @Expose
    private String chatGPTGeneratedText;

    @SerializedName("PriceChangeNotificationType")
    @Expose
    private PriceChangeNotificationType priceChangeNotificationType;

    @SerializedName("Percentage")
    @Expose
    private int percentage;

    @SerializedName("Amount")
    @Expose
    private int amount;

    public InTripDTO(String itineraryName, int adults, OutFlightDealDTO outFlightDealDTO, String chatGPTGeneratedText, PriceChangeNotificationType priceChangeNotificationType, int percentage, int amount) {
        this.itineraryName = itineraryName;
        this.adults = adults;
        this.outFlightDealDTO = outFlightDealDTO;
        this.chatGPTGeneratedText = chatGPTGeneratedText;
        this.priceChangeNotificationType = priceChangeNotificationType;
        this.percentage = percentage;
        this.amount = amount;
    }

}
