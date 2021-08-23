package org.cise.sdk.ciseapp.modules.adapter.components;
//
// Created by  on 2019-12-24.
//

import android.view.View;
import android.widget.TextView;

import org.cise.core.utilities.ui.adapter.recyclerview.HolderGeneric;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.models.SampleData;
import org.cise.sdk.ciseapp.modules.adapter.model.SampleDataMV;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HolderSampleData extends HolderGeneric<SampleDataMV> {

    @BindView(R.id.name)
    TextView sampleName;

    public HolderSampleData(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    protected void onBindViewHolder(SampleDataMV o) {
        super.onBindViewHolder(o);
        sampleName.setText(o.getModel().getName());
    }

    public interface Listener extends HolderGeneric.Listener {

    }

}
