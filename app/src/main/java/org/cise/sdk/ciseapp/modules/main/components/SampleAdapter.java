package org.cise.sdk.ciseapp.modules.main.components;

import org.cise.core.utilities.ui.adapter.recyclerview.AdapterGeneric;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.main.models.SampleMV;

public class SampleAdapter extends AdapterGeneric<SampleMV> {

    @Override
    protected void registerHolder() {
        register(R.layout.adapter_sample, SampleHolder.class);
    }
}
