package com.flightsearch.utils.models.out;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OutUserDTO {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("birthday")
    @Expose
    private String birthday;

    @SerializedName("preferences")
    @Expose
    private String preferences;

    @SerializedName("friends")
    @Expose
    private List<OutUserDTO> friends;

    @SerializedName("requests")
    @Expose
    private List<OutUserDTO> requests;

    @SerializedName("pending")
    @Expose
    private List<OutUserDTO> pending;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public List<OutUserDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<OutUserDTO> friends) {
        this.friends = friends;
    }

    public List<OutUserDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<OutUserDTO> requests) {
        this.requests = requests;
    }

    public List<OutUserDTO> getPending() {
        return pending;
    }

    public void setPending(List<OutUserDTO> pending) {
        this.pending = pending;
    }
}
