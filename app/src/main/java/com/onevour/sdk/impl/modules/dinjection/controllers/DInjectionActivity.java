package com.onevour.sdk.impl.modules.dinjection.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.onevour.core.utilities.commons.RefSession;
import com.onevour.sdk.impl.MainApplication;
import com.onevour.sdk.impl.R;
import com.onevour.sdk.impl.databinding.ActivityDinjectionBinding;

import javax.inject.Inject;

/**
 * https://developer.android.com/training/dependency-injection<br/>
 * https://guides.codepath.com/android/dependency-injection-with-dagger-2<br/>
 */

public class DInjectionActivity extends AppCompatActivity {

    ActivityDinjectionBinding binding;

    @Inject
    RefSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDinjectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MainApplication.component.inject(this);
        session.saveInt("key", 100);
        loadView();
    }

    private void loadView() {
        int key = session.findInt("key");
        binding.info.setText(String.valueOf(key));
    }

}