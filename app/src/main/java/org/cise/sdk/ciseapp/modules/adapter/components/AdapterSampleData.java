package org.cise.sdk.ciseapp.modules.adapter.components;
//
// Created by  on 2019-12-24.
//

import org.cise.core.utilities.ui.adapter.recyclerview.AdapterGeneric;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.models.SampleData;
import org.cise.sdk.ciseapp.modules.adapter.model.SampleDataMV;

public class AdapterSampleData extends AdapterGeneric<SampleDataMV> {

    @Override
    protected void registerHolder() {
        register(R.layout.adapter_sample_data, HolderSampleData.class);
    }
}
