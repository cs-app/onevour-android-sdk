package org.cise.sdk.ciseapp.modules.main.components;

import org.cise.core.utilities.ui.adapter.recyclerview.GenericAdapter;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.main.models.Sample;

public class SampleAdapter extends GenericAdapter<SampleHolder, Sample> {

    public SampleAdapter() {
        super(R.layout.adapter_sample);
    }

}
