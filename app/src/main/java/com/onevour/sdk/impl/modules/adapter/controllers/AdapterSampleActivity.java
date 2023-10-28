package com.onevour.sdk.impl.modules.adapter.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.onevour.core.utilities.beans.BeanCopy;
import com.onevour.core.utilities.helper.UIHelper;
import com.onevour.core.components.recycleview.AdapterGeneric;
import com.onevour.core.components.recycleview.RecyclerViewScrollListener;

import com.onevour.sdk.impl.databinding.ActivityMasterSampleBinding;
import com.onevour.sdk.impl.modules.adapter.components.AdapterSampleData;
import com.onevour.sdk.impl.SampleData;
import com.onevour.sdk.impl.modules.adapter.model.SampleDataMV;

import java.util.ArrayList;
import java.util.List;


public class AdapterSampleActivity extends AppCompatActivity implements AdapterGeneric.AdapterListener<SampleData>, RecyclerViewScrollListener.PaginationListener<SampleData>, AdapterSampleData.HolderSampleData.Listener {

    private String TAG = "MA-APP-AD";

    AdapterSampleData adapter = new AdapterSampleData();

    ActivityMasterSampleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMasterSampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UIHelper.initRecyclerView(binding.rvSampleData, adapter, this, true);
        adapter.setHolderListener(this);
        init();
    }

    private void init() {
        List<SampleDataMV> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new SampleDataMV(1, new SampleData(i, "next : ".concat(String.valueOf(i)))));
//            if (i % 2 == 0) {
//            } else {
//                list.add(new SampleDataMV(2, new SampleData(i, "next : ".concat(String.valueOf(i)))));
//            }
        }
        adapter.addMore(list);
    }

    private void requestNextPage(boolean resultSuccess) {
        if (Looper.myLooper() == null) Looper.prepare();
        new Handler().postDelayed(() -> {
            List<SampleDataMV> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(new SampleDataMV(new SampleData("next : ".concat(String.valueOf(i)))));
            }
            if (resultSuccess) {
                adapter.setValue(list);
            } else {
//                adapter.er("Error, tap for reload");
            }

        }, 2000);
    }

    @Override
    public void onLoadRetry(int index, SampleData o) {
        requestNextPage(true);
        Log.d(TAG, "retry load data from last :  from index: ".concat(String.valueOf(index)));
    }

    @Override
    public void loadMoreItems(SampleData sampleData) {
        requestNextPage(false);
        Log.d(TAG, "load more from last value: ".concat(sampleData.getName()));

    }

    @Override
    public void updateAge(int index, int age) {
        Toast.makeText(this, index + " age: " + age, Toast.LENGTH_SHORT).show();
        List<SampleDataMV> values = adapter.getAdapterList();
        SampleDataMV o = new SampleDataMV(1);
        // SampleData sampleData = new SampleData(index, "next " + index);
        SampleData sampleData = BeanCopy.value(values.get(index).getModel(), SampleData.class);
        sampleData.setAge(age);
        o.setModel(sampleData);
        values.set(index, o);
        adapter.setValue(values, false);
    }


}
