package org.cise.sdk.ciseapp.modules.main.models;
//
// Created by Zuliadin on 2019-12-24.
//

public class Sample {

    private String sample;

    private Class clazz;

    public Sample(String sample, Class clazz) {
        this.sample = sample;
        this.clazz = clazz;
    }

    public String getSample() {
        return sample;
    }

    public void setSample(String sample) {
        this.sample = sample;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
