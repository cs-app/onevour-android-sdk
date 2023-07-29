package com.onevour.sdk.impl.modules.main.components;

import android.view.View;

import com.onevour.core.utilities.commons.ValueOf;
import com.onevour.core.components.recycleview.AdapterGeneric;
import com.onevour.core.components.recycleview.HolderGeneric;
import com.onevour.sdk.impl.databinding.AdapterSampleBinding;
import com.onevour.sdk.impl.modules.main.models.Sample;
import com.onevour.sdk.impl.modules.main.models.SampleMV;

public class SampleAdapter extends AdapterGeneric<SampleMV> {

    @Override
    protected void registerHolder() {
        registerBindView(SampleHolder.class);
    }

    public static class SampleHolder extends HolderGeneric<AdapterSampleBinding, SampleMV> implements View.OnClickListener {


        public SampleHolder(AdapterSampleBinding binding) {
            super(binding);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        protected void onBindViewHolder(SampleMV model, int position) {
            super.onBindViewHolder(model, position);
            binding.name.setText(model.getModel().getSample());
        }

        @Override
        public void onClick(View v) {
            Listener listener = getListener(Listener.class);
            if (ValueOf.isNull(listener)) return;
            listener.onSelectedHolder(getCurrentPosition(), value.getModel());
        }

        public interface Listener extends HolderGeneric.Listener {

            void onSelectedHolder(int index, Sample o);

        }

    }
}
