package com.onevour.sdk.impl.modules.main.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.onevour.core.base.BaseActivity;
import com.onevour.core.utilities.commons.ContextHelper;
import com.onevour.core.utilities.commons.ValueOf;
import com.onevour.sdk.impl.databinding.ActivityMainBinding;
import com.onevour.sdk.impl.modules.adapter.controllers.AdapterSampleActivity;
import com.onevour.sdk.impl.modules.chat.ChatActivity;
import com.onevour.sdk.impl.modules.form.controllers.FormSimpleActivity;
import com.onevour.sdk.impl.modules.formscroll.controllers.FormScrollActivity;
import com.onevour.sdk.impl.modules.fragment.controllers.FragmentActivity;
import com.onevour.sdk.impl.modules.fragmentbottom.controllers.FragmentBottomActivity;
import com.onevour.sdk.impl.modules.fragmentbottomnavigation.controllers.FragmentBottomNavigationActivity;
import com.onevour.sdk.impl.modules.main.components.SampleAdapter;
import com.onevour.sdk.impl.modules.main.models.Sample;
import com.onevour.sdk.impl.modules.main.models.SampleMV;
import com.onevour.sdk.impl.modules.mvvm.views.MVVMActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * update by Zuliadin at 2023-07-29
 */
public class MainActivity extends BaseActivity implements SampleAdapter.SampleHolder.Listener {

    private String TAG = MainActivity.class.getSimpleName();

    private SampleAdapter adapter = new SampleAdapter();

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContextHelper.init(getApplication());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        init(binding.rvSample, adapter);
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
