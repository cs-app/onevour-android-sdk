package com.onevour.core.utilities.jwt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JWTTokenRefreshResponse {

    @Expose
    @SerializedName("access_token")
    String accessToken;

    @Expose
    @SerializedName("refresh_token")
    String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
