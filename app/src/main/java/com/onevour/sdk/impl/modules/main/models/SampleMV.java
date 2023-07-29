package com.onevour.sdk.impl.modules.main.models;

import com.onevour.core.components.recycleview.AdapterModel;

public class SampleMV extends AdapterModel<Sample> {

    public SampleMV(Sample model) {
        super(model);
    }

    public SampleMV(String sample, Class clazz) {
        super(new Sample(sample, clazz));
    }

}
