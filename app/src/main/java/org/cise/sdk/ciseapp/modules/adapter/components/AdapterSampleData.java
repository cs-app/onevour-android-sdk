package org.cise.sdk.ciseapp.modules.adapter.components;
//
// Created by  on 2019-12-24.
//

import org.cise.core.utilities.ui.adapter.recyclerview.GenericAdapter;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.models.SampleData;

public class AdapterSampleData extends GenericAdapter<HolderSampleData, SampleData> {

    public AdapterSampleData() {
        super(R.layout.adapter_sample_data);
    }
}
