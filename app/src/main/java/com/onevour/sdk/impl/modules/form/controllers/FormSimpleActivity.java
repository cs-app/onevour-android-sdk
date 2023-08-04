package com.onevour.sdk.impl.modules.form.controllers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.onevour.core.utilities.format.NFormat;
import com.onevour.core.utilities.input.NumberInput;
import com.onevour.sdk.impl.databinding.ActivityFormSimpleBinding;


public class FormSimpleActivity extends AppCompatActivity {

    NumberInput number = new NumberInput();

    NumberInput decimal = new NumberInput();

    ActivityFormSimpleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormSimpleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.inputNumber.setText(String.valueOf(Integer.MAX_VALUE));
        binding.inputDecimal.setText(NFormat.currencyFormat(5603169.26));

        number.setup(binding.inputNumber, 0, Integer.MAX_VALUE);
        // number.enableMaxValue(); 5603169.26
        decimal.setup(binding.inputDecimal, NFormat.currency(), 0, Double.MAX_VALUE);
        // decimal.setup(binding.inputDecimal, NFormat.currency(), 0, 10000000.1);
        // decimal.enableMaxValue();
    }
}
