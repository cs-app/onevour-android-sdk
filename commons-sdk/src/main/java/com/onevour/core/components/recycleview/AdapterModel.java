package com.onevour.core.components.recycleview;

import com.google.gson.annotations.Expose;

public class AdapterModel<T> {

    @Expose
    private int type = 1;

    @Expose
    private T model;

    public AdapterModel(int type) {
        this.type = type;
    }

    public AdapterModel(T model) {
        this.model = model;
    }

    public AdapterModel(int type, T model) {
        this.type = type;
        this.model = model;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
