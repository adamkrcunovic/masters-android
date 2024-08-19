package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutCommentDTO {

    @SerializedName("commentText")
    @Expose
    private String commentText;

    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;

    @SerializedName("user")
    @Expose
    private OutUserDTO user;

    public OutCommentDTO(String commentText, String dateCreated, OutUserDTO user) {
        this.commentText = commentText;
        this.dateCreated = dateCreated;
        this.user = user;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OutUserDTO getUser() {
        return user;
    }

    public void setUser(OutUserDTO user) {
        this.user = user;
    }
}
