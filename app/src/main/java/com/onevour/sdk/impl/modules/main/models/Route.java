package com.onevour.sdk.impl.modules.main.models;
//
// Created by  on 2019-10-22.
//

public class Route {

    //{"routeId":9506}
    long routeId;

    private String userId;

    public long getRouteId() {
        return routeId;
    }

    public void setRouteId(long routeId) {
        this.routeId = routeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
