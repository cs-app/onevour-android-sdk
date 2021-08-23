package org.cise.sdk.ciseapp.modules.main.models;

import org.cise.core.utilities.ui.adapter.recyclerview.AdapterModel;

public class SampleMV extends AdapterModel<Sample> {

    public SampleMV(Sample model) {
        super(model);
    }
    public SampleMV(String sample, Class clazz) {
        super(new Sample(sample, clazz));
    }
}
