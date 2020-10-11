package org.cise.core.utilities.ui.adapter.recyclerview;

public class AdapterModel<T> {

    private int type = 1;

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
