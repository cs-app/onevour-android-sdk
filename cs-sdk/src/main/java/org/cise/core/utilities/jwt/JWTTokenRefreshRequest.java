package org.cise.core.utilities.jwt;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class JWTTokenRefreshRequest {

    @Expose
    @SerializedName("refresh_token")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
