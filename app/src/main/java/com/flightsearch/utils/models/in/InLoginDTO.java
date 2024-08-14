package com.flightsearch.utils.models.in;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InLoginDTO {

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("Password")
    @Expose
    private String password;

    @SerializedName("DeviceId")
    @Expose
    private String deviceId;

    public InLoginDTO(String email, String password, String deviceId) {
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }
}
