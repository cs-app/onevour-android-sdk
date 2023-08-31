package com.onevour.sdk.impl.modules.bluetooth.components;

import com.onevour.core.components.recycleview.AdapterGenericBasic;
import com.onevour.core.components.recycleview.HolderGenericBasic;
import com.onevour.sdk.impl.databinding.AdapterSampleBinding;

public class AdapterMessage extends AdapterGenericBasic<AdapterMessage.HolderMessage, String> {

    public static class HolderMessage extends HolderGenericBasic<AdapterSampleBinding, String> {

        public HolderMessage(AdapterSampleBinding binding) {
            super(binding);
        }

        @Override
        protected void onBindViewHolder(String o) {
            binding.name.setText(o);
        }
    }
}
