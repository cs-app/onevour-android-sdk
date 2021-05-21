package org.cise.sdk.ciseapp.modules.adapter.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.cise.core.utilities.helper.UIHelper;
import org.cise.core.utilities.ui.adapter.recyclerview.AdapterGeneric;
import org.cise.core.utilities.ui.adapter.recyclerview.RecyclerViewScrollListener;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.adapter.components.AdapterSampleData;
import org.cise.sdk.ciseapp.modules.adapter.components.HolderSampleData;
import org.cise.sdk.ciseapp.models.SampleData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterSampleActivity extends AppCompatActivity implements AdapterGeneric.AdapterListener<SampleData>, RecyclerViewScrollListener.PaginationListener<SampleData> {

    private String TAG = "MA-APP-AD";

    @BindView(R.id.rv_sample_data)
    RecyclerView recyclerView;

    AdapterSampleData adapter = new AdapterSampleData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_sample);
        ButterKnife.bind(this);
        UIHelper.initRecyclerView(recyclerView, adapter, this, this, true);
        init();
    }

    private void init() {
        List<SampleData> sampleDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            sampleDatas.add(new SampleData("Sample Data " + (i + 1)));
        }
//        adapter.setValue(sampleDatas);
        new Handler().postDelayed(() -> {
//            adapter.setError("Error, tap for reload");
        }, 1000);
    }

    private void requestNextPage(boolean resultSuccess) {
        if (Looper.myLooper() == null) Looper.prepare();
        new Handler().postDelayed(() -> {
            List<SampleData> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(new SampleData("next : " + i));
            }
            if (resultSuccess) {
//                adapter.addMore(list);
            } else {
//                adapter.setError("Error, tap for reload");
            }
        }, 2000);
    }

    @Override
    public void onLoadRetry(int index, SampleData o) {
        requestNextPage(true);
        Log.d(TAG, "retry load data from last : " + o + " from index: " + index);
    }

    @Override
    public void loadMoreItems(SampleData sampleData) {
        requestNextPage(false);
        Log.d(TAG, "load more from last value: " + sampleData.getName());

    }
}
