package com.onevour.sdk.impl.modules.adapter.components;
//
// Created by  on 2019-12-24.
//

import android.view.View;

import com.onevour.core.components.recycleview.AdapterGeneric;
import com.onevour.core.components.recycleview.HolderGeneric;
import com.onevour.sdk.impl.databinding.AdapterSampleDataBinding;
import com.onevour.sdk.impl.databinding.AdapterSampleDataColorBinding;
import com.onevour.sdk.impl.modules.adapter.model.SampleDataMV;

import java.util.Objects;

public class AdapterSampleData extends AdapterGeneric<SampleDataMV> {

    @Override
    protected void registerHolder() {
        registerBindView(1, HolderSampleData.class);
        registerBindView(2, HolderSampleDataRed.class);
    }

    public static class HolderSampleData extends HolderGeneric<AdapterSampleDataBinding, SampleDataMV> {

        public HolderSampleData(AdapterSampleDataBinding binding) {
            super(binding);
            binding.getRoot().setOnClickListener(this::selected);
        }

        private void selected(View view) {
            Listener listener = getListener(Listener.class);
            if (Objects.isNull(listener)) return;
            listener.onSelectedText(value.getModel().getName());
        }

        @Override
        protected void onBindViewHolder(SampleDataMV o) {
            super.onBindViewHolder(o);
            binding.name.setText(value.getModel().getName());
        }

        public interface Listener extends HolderGeneric.Listener {

            void onSelectedText(String value);

        }

    }

    public static class HolderSampleDataRed extends HolderGeneric<AdapterSampleDataColorBinding, SampleDataMV> {

        public HolderSampleDataRed(AdapterSampleDataColorBinding binding) {
            super(binding);
            binding.getRoot().setOnClickListener(this::selected);
        }

        private void selected(View view) {
            Listener listener = getListener(Listener.class);
            if (Objects.isNull(listener)) return;
            listener.onSelectedText(value.getModel().getName());
        }

        @Override
        protected void onBindViewHolder(SampleDataMV o) {
            super.onBindViewHolder(o);
            binding.name.setText(value.getModel().getName());
        }

        public interface Listener extends HolderGeneric.Listener {

            void onSelectedText(String value);

        }

    }

}
