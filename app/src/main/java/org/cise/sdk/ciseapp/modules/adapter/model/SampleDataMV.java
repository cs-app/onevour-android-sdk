package org.cise.sdk.ciseapp.modules.adapter.model;

import org.cise.core.utilities.ui.adapter.recyclerview.AdapterModel;
import org.cise.sdk.ciseapp.models.SampleData;

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
