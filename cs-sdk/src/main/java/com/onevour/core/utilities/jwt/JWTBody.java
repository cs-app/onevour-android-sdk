package com.onevour.core.utilities.jwt;

import com.google.gson.annotations.Expose;


public class JWTBody {

    @Expose
    String sub;

    @Expose
    Long iat;

    @Expose
    Long exp;

    public String getSub() {
        return sub;
    }

    public Long getIat() {
        return iat;
    }

    public Long getExp() {
        return exp;
    }
}
