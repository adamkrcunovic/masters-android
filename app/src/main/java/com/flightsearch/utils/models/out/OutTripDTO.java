package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OutTripDTO extends OutFlightDealDTO {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("itineraryName")
    @Expose
    private String itineraryName;

    @SerializedName("adults")
    @Expose
    private int adults;

    @SerializedName("currentPrice")
    @Expose
    private Double currentPrice;

    @SerializedName("chatGPTGeneratedText")
    @Expose
    private String chatGPTGeneratedText;

    @SerializedName("invitedMembers")
    @Expose
    public List<OutUserDTO> invitedMembers;

    @SerializedName("creator")
    @Expose
    public OutUserDTO creator;

    @SerializedName("comments")
    @Expose
    public List<OutCommentDTO> comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItineraryName() {
        return itineraryName;
    }

    public void setItineraryName(String itineraryName) {
        this.itineraryName = itineraryName;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getChatGPTGeneratedText() {
        return chatGPTGeneratedText;
    }

    public void setChatGPTGeneratedText(String chatGPTGeneratedText) {
        this.chatGPTGeneratedText = chatGPTGeneratedText;
    }

    public List<OutUserDTO> getInvitedMembers() {
        return invitedMembers;
    }

    public void setInvitedMembers(List<OutUserDTO> invitedMembers) {
        this.invitedMembers = invitedMembers;
    }

    public OutUserDTO getCreator() {
        return creator;
    }

    public void setCreator(OutUserDTO creator) {
        this.creator = creator;
    }

    public List<OutCommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<OutCommentDTO> comments) {
        this.comments = comments;
    }
}
