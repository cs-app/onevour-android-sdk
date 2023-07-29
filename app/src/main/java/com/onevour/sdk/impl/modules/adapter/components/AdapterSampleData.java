package com.onevour.sdk.impl.modules.adapter.components;
//
// Created by  on 2019-12-24.
//

import com.onevour.core.components.recycleview.AdapterGeneric;
import com.onevour.core.components.recycleview.HolderGeneric;
import com.onevour.sdk.impl.databinding.AdapterSampleDataBinding;
import com.onevour.sdk.impl.modules.adapter.model.SampleDataMV;

public class AdapterSampleData extends AdapterGeneric<SampleDataMV> {

    @Override
    protected void registerHolder() {
        registerBindView(HolderSampleData.class);
    }

    public static class HolderSampleData extends HolderGeneric<AdapterSampleDataBinding, SampleDataMV> {

        public HolderSampleData(AdapterSampleDataBinding binding) {
            super(binding);
        }

        @Override
        protected void onBindViewHolder(SampleDataMV o) {
            super.onBindViewHolder(o);
            binding.name.setText(value.getModel().getName());
        }

        public interface Listener extends HolderGeneric.Listener {

        }

    }

}
