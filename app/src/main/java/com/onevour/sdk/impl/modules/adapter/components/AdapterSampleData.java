package com.onevour.sdk.impl.modules.adapter.components;
//
// Created by  on 2019-12-24.
//

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

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
        registerAsyncListDiffer(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull SampleDataMV oldItem, @NonNull SampleDataMV newItem) {
                return oldItem.getModel().getId() == newItem.getModel().getId();
//                return false;
            }

            @Override
            public boolean areContentsTheSame(@NonNull SampleDataMV oldItem, @NonNull SampleDataMV newItem) {
                return oldItem.getModel().getAge() == newItem.getModel().getAge();
//                return false;
            }

        });
    }

    public static class HolderSampleData extends HolderGeneric<AdapterSampleDataBinding, SampleDataMV> implements View.OnClickListener {

        public HolderSampleData(AdapterSampleDataBinding binding) {
            super(binding);
            binding.add.setOnClickListener(this);
        }

        @Override
        protected void onBindViewHolder(SampleDataMV o) {
            super.onBindViewHolder(o);
            binding.name.setText(value.getModel().getName());
            binding.age.setText(String.valueOf(value.getModel().getAge()));
        }

        @Override
        public void onClick(View v) {
            Listener listener = getListener(Listener.class);
            if (Objects.isNull(listener)) return;
            listener.updateAge(getCurrentPosition(), value);
        }

        public interface Listener extends HolderGeneric.Listener {

            void updateAge(int index, SampleDataMV sampleDataMV);

        }

    }

    public static class HolderSampleDataRed extends HolderGeneric<AdapterSampleDataColorBinding, SampleDataMV> {

        public HolderSampleDataRed(AdapterSampleDataColorBinding binding) {
            super(binding);
        }

        @Override
        protected void onBindViewHolder(SampleDataMV o) {
            super.onBindViewHolder(o);
            binding.name.setText(value.getModel().getName());
        }

        private void selected(View view) {
            Listener listener = getListener(Listener.class);
            if (Objects.isNull(listener)) return;
            listener.onSelectedText(value.getModel().getName());
        }

        public interface Listener extends HolderGeneric.Listener {

            void onSelectedText(String value);

        }

    }

}
