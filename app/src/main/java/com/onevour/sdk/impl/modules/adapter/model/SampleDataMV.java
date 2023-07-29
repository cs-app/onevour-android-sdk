package com.onevour.sdk.impl.modules.adapter.model;

import com.onevour.core.components.recycleview.AdapterModel;
import com.onevour.sdk.impl.SampleData;

public class SampleDataMV extends AdapterModel<SampleData> {
    public SampleDataMV(int type) {
        super(type);
    }

    public SampleDataMV(SampleData model) {
        super(model);
    }

    public SampleDataMV(int type, SampleData model) {
        super(type, model);
    }
}
