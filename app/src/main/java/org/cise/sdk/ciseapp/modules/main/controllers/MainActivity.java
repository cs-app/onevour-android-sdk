package org.cise.sdk.ciseapp.modules.main.controllers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.cise.core.utilities.helper.UIHelper;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.form.controllers.FormSimpleActivity;
import org.cise.sdk.ciseapp.modules.formscroll.controllers.FormScrollActivity;
import org.cise.sdk.ciseapp.modules.fragmentbottom.controllers.FragmentBottomActivity;
import org.cise.sdk.ciseapp.modules.fragment.controllers.FragmentActivity;
import org.cise.sdk.ciseapp.modules.fragmentbottomnavigation.controllers.FragmentBottomNavigationActivity;
import org.cise.sdk.ciseapp.modules.main.components.SampleAdapter;
import org.cise.sdk.ciseapp.modules.main.components.SampleHolder;
import org.cise.sdk.ciseapp.modules.main.models.Sample;
import org.cise.sdk.ciseapp.modules.adapter.controllers.AdapterSampleActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SampleHolder.Listener {

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
        samples.add(new Sample("Adapter", AdapterSampleActivity.class));
        samples.add(new Sample("Fragment BackStack", FragmentActivity.class));
        samples.add(new Sample("Fragment Bottom", FragmentBottomActivity.class));
        samples.add(new Sample("Fragment Bottom Navigation", FragmentBottomNavigationActivity.class));
        samples.add(new Sample("Form Simple", FormSimpleActivity.class));
        samples.add(new Sample("Form Scroll", FormScrollActivity.class));
        adapter.setValue(samples);
    }

    @Override
    public void onSelectedHolder(int index, Sample o) {
        startActivity(new Intent(this, o.getClazz()));
    }


}
