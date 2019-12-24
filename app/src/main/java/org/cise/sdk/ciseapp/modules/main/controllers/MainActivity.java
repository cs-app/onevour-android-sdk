package org.cise.sdk.ciseapp.modules.main.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.cise.core.utilities.helper.UIHelper;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.main.components.SampleAdapter;
import org.cise.sdk.ciseapp.modules.main.components.SampleHolder;
import org.cise.sdk.ciseapp.modules.main.models.Sample;
import org.cise.sdk.ciseapp.modules.sample.controllers.MasterSampleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SampleHolder.Listener {

    // implements RecyclerViewScrollListener.RecyclerViewPaginationListener<String>, GenericAdapter.AdapterListener<String>

    private String TAG = "MA-APP";

    @BindView(R.id.rv_sample)
    RecyclerView rvSample;

    private SampleAdapter adapter = new SampleAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        UIHelper.initRecyclerView(rvSample, adapter);
        adapter.setHolderListener(this);
        init();
    }

    private void init() {
        List<Sample> samples = new ArrayList<>();
        samples.add(new Sample("Adapter", MasterSampleActivity.class));
        adapter.setValue(samples);
    }

    @Override
    public void onSelectedHolder(int index, Sample o) {
        startActivity(new Intent(this, o.getClazz()));
    }




    /*

    public void requestNextPage(boolean resultSuccess) {
        if (Looper.myLooper() == null) Looper.prepare();
        new Handler().postDelayed(() -> {
            List<String> ist = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ist.add("next : " + i);
            }
            if (resultSuccess) {
                //adapter.addMore(ist);
            } else {
                adapter.setError("Error");
            }
        }, 3000);

    }

    private void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMoreItems(String o) {
        requestNextPage(false);
        Log.d(TAG, "load more from last value: " + o);
    }

    @Override
    public void onLoadRetry(int index, String o) {
        requestNextPage(true);
        Log.d(TAG, "retry load data from last : " + o + " from index: " + index);
    }
     */
}
