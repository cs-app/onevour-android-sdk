package org.cise.sdk.ciseapp.modules.main.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import org.cise.core.base.BaseActivity;
import org.cise.core.utilities.commons.ContextHelper;
import org.cise.core.utilities.commons.ValueOf;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.modules.adapter.controllers.AdapterSampleActivity;
import org.cise.sdk.ciseapp.modules.chat.ChatActivity;
import org.cise.sdk.ciseapp.modules.form.controllers.FormSimpleActivity;
import org.cise.sdk.ciseapp.modules.formscroll.controllers.FormScrollActivity;
import org.cise.sdk.ciseapp.modules.fragment.controllers.FragmentActivity;
import org.cise.sdk.ciseapp.modules.fragmentbottom.controllers.FragmentBottomActivity;
import org.cise.sdk.ciseapp.modules.fragmentbottomnavigation.controllers.FragmentBottomNavigationActivity;
import org.cise.sdk.ciseapp.modules.main.components.SampleAdapter;
import org.cise.sdk.ciseapp.modules.main.components.SampleHolder;
import org.cise.sdk.ciseapp.modules.main.models.Sample;
import org.cise.sdk.ciseapp.modules.main.models.SampleMV;
import org.cise.sdk.ciseapp.modules.mvvm.views.MVVMActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements SampleHolder.Listener {

    private String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_sample)
    RecyclerView rvSample;

    private SampleAdapter adapter = new SampleAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextHelper.init(getApplication());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init(rvSample, adapter);
        adapter.setHolderListener(this);
        List<SampleMV> samples = new ArrayList<>();
        samples.add(new SampleMV("Adapter", AdapterSampleActivity.class));
        samples.add(new SampleMV("Fragment BackStack", FragmentActivity.class));
        samples.add(new SampleMV("Fragment Bottom", FragmentBottomActivity.class));
        samples.add(new SampleMV("Fragment Bottom Navigation", FragmentBottomNavigationActivity.class));
        samples.add(new SampleMV("Form Simple", FormSimpleActivity.class));
        samples.add(new SampleMV("Form Scroll", FormScrollActivity.class));
        samples.add(new SampleMV("MVVM", MVVMActivity.class));
        samples.add(new SampleMV("Chat", ChatActivity.class));
        adapter.setValue(samples);
        session.saveCollection("MENU", samples);
        init();
    }

    private void init() {
        List<SampleMV> samples = session.findCollection("MENU", SampleMV.class);
        if (ValueOf.isNull(samples)) return;
        Log.d(TAG, "list size from ref session " + samples.size());
    }

    @Override
    public void onSelectedHolder(int index, Sample o) {
        startActivity(new Intent(this, o.getClazz()));
    }


}
