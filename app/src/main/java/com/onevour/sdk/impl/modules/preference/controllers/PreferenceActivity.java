package com.onevour.sdk.impl.modules.preference.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.onevour.core.utilities.commons.RefSession;
import com.onevour.sdk.impl.R;
import com.onevour.sdk.impl.databinding.ActivityPreferenceBinding;

public class PreferenceActivity extends AppCompatActivity {

    private RefSession session = new RefSession();

    private ActivityPreferenceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreferenceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        session.saveString("str", "string");
        session.saveBoolean("bol", true);
        session.saveInt("int", 10);
        session.saveFloat("float", 10F);
        session.saveLong("long", 10L);
        session.saveDouble("double", 10.0);
        loadOnView();
    }

    private void loadOnView() {
        String strValue = session.findString("STR");
        boolean bolValue = session.findBoolean("bol");
        int intValue = session.findInt("int");
        float floatValue = session.findFloat("float");
        long longValue = session.findLong("long");
        double doubleValue = session.findDouble("double");
        //
        binding.string.setText(strValue);
        binding.bool.setText(String.valueOf(bolValue));
        binding.integer.setText(String.valueOf(intValue));
        binding.longs.setText(String.valueOf(longValue));
        binding.floats.setText(String.valueOf(floatValue));
        binding.doubles.setText(String.valueOf(doubleValue));
    }
}