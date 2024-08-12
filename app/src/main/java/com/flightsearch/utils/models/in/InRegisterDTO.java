package com.flightsearch.utils.models.in;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InRegisterDTO {

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Password")
    @Expose
    private String password;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("LastName")
    @Expose
    private String lastName;

    @SerializedName("Birthday")
    @Expose
    private String birthday;

    @SerializedName("DeviceId")
    @Expose
    private String deviceId;

    @SerializedName("CountryId")
    @Expose
    private int countryId;

    @SerializedName("Preferences")
    @Expose
    private String preferences;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}

